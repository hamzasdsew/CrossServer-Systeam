/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JedisPubSubBase<T>
/*     */ {
/*  14 */   private int subscribedChannels = 0;
/*     */   
/*     */   private volatile Connection client;
/*     */ 
/*     */   
/*     */   public void onMessage(T channel, T message) {}
/*     */ 
/*     */   
/*     */   public void onPMessage(T pattern, T channel, T message) {}
/*     */ 
/*     */   
/*     */   public void onSubscribe(T channel, int subscribedChannels) {}
/*     */ 
/*     */   
/*     */   public void onUnsubscribe(T channel, int subscribedChannels) {}
/*     */ 
/*     */   
/*     */   public void onPUnsubscribe(T pattern, int subscribedChannels) {}
/*     */ 
/*     */   
/*     */   public void onPSubscribe(T pattern, int subscribedChannels) {}
/*     */   
/*     */   public void onPong(T pattern) {}
/*     */   
/*     */   private void sendAndFlushCommand(Protocol.Command command, T... args) {
/*  39 */     if (this.client == null) {
/*  40 */       throw new JedisException(getClass() + " is not connected to a Connection.");
/*     */     }
/*  42 */     CommandArguments cargs = (new CommandArguments(command)).addObjects((Object[])args);
/*  43 */     this.client.sendCommand(cargs);
/*  44 */     this.client.flush();
/*     */   }
/*     */   
/*     */   public final void unsubscribe() {
/*  48 */     sendAndFlushCommand(Protocol.Command.UNSUBSCRIBE, (T[])new Object[0]);
/*     */   }
/*     */   
/*     */   public final void unsubscribe(T... channels) {
/*  52 */     sendAndFlushCommand(Protocol.Command.UNSUBSCRIBE, channels);
/*     */   }
/*     */   
/*     */   public final void subscribe(T... channels) {
/*  56 */     sendAndFlushCommand(Protocol.Command.SUBSCRIBE, channels);
/*     */   }
/*     */   
/*     */   public final void psubscribe(T... patterns) {
/*  60 */     sendAndFlushCommand(Protocol.Command.PSUBSCRIBE, patterns);
/*     */   }
/*     */   
/*     */   public final void punsubscribe() {
/*  64 */     sendAndFlushCommand(Protocol.Command.PUNSUBSCRIBE, (T[])new Object[0]);
/*     */   }
/*     */   
/*     */   public final void punsubscribe(T... patterns) {
/*  68 */     sendAndFlushCommand(Protocol.Command.PUNSUBSCRIBE, patterns);
/*     */   }
/*     */   
/*     */   public final void ping() {
/*  72 */     sendAndFlushCommand(Protocol.Command.PING, (T[])new Object[0]);
/*     */   }
/*     */   
/*     */   public final void ping(T argument) {
/*  76 */     sendAndFlushCommand(Protocol.Command.PING, (T[])new Object[] { argument });
/*     */   }
/*     */   
/*     */   public final boolean isSubscribed() {
/*  80 */     return (this.subscribedChannels > 0);
/*     */   }
/*     */   
/*     */   public final int getSubscribedChannels() {
/*  84 */     return this.subscribedChannels;
/*     */   }
/*     */   
/*     */   public final void proceed(Connection client, T... channels) {
/*  88 */     this.client = client;
/*  89 */     this.client.setTimeoutInfinite();
/*     */     try {
/*  91 */       subscribe(channels);
/*  92 */       process();
/*     */     } finally {
/*  94 */       this.client.rollbackTimeout();
/*     */     } 
/*     */   }
/*     */   
/*     */   public final void proceedWithPatterns(Connection client, T... patterns) {
/*  99 */     this.client = client;
/* 100 */     this.client.setTimeoutInfinite();
/*     */     try {
/* 102 */       psubscribe(patterns);
/* 103 */       process();
/*     */     } finally {
/* 105 */       this.client.rollbackTimeout();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T encode(byte[] paramArrayOfbyte);
/*     */ 
/*     */   
/*     */   private void process() {
/*     */     do {
/* 115 */       Object reply = this.client.getUnflushedObject();
/*     */       
/* 117 */       if (reply instanceof List) {
/* 118 */         List<Object> listReply = (List<Object>)reply;
/* 119 */         Object firstObj = listReply.get(0);
/* 120 */         if (!(firstObj instanceof byte[])) {
/* 121 */           throw new JedisException("Unknown message type: " + firstObj);
/*     */         }
/* 123 */         byte[] resp = (byte[])firstObj;
/* 124 */         if (Arrays.equals(Protocol.ResponseKeyword.SUBSCRIBE.getRaw(), resp)) {
/* 125 */           this.subscribedChannels = ((Long)listReply.get(2)).intValue();
/* 126 */           byte[] bchannel = (byte[])listReply.get(1);
/* 127 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/* 128 */           onSubscribe(enchannel, this.subscribedChannels);
/* 129 */         } else if (Arrays.equals(Protocol.ResponseKeyword.UNSUBSCRIBE.getRaw(), resp)) {
/* 130 */           this.subscribedChannels = ((Long)listReply.get(2)).intValue();
/* 131 */           byte[] bchannel = (byte[])listReply.get(1);
/* 132 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/* 133 */           onUnsubscribe(enchannel, this.subscribedChannels);
/* 134 */         } else if (Arrays.equals(Protocol.ResponseKeyword.MESSAGE.getRaw(), resp)) {
/* 135 */           byte[] bchannel = (byte[])listReply.get(1);
/* 136 */           byte[] bmesg = (byte[])listReply.get(2);
/* 137 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/* 138 */           T enmesg = (bmesg == null) ? null : encode(bmesg);
/* 139 */           onMessage(enchannel, enmesg);
/* 140 */         } else if (Arrays.equals(Protocol.ResponseKeyword.PMESSAGE.getRaw(), resp)) {
/* 141 */           byte[] bpattern = (byte[])listReply.get(1);
/* 142 */           byte[] bchannel = (byte[])listReply.get(2);
/* 143 */           byte[] bmesg = (byte[])listReply.get(3);
/* 144 */           T enpattern = (bpattern == null) ? null : encode(bpattern);
/* 145 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/* 146 */           T enmesg = (bmesg == null) ? null : encode(bmesg);
/* 147 */           onPMessage(enpattern, enchannel, enmesg);
/* 148 */         } else if (Arrays.equals(Protocol.ResponseKeyword.PSUBSCRIBE.getRaw(), resp)) {
/* 149 */           this.subscribedChannels = ((Long)listReply.get(2)).intValue();
/* 150 */           byte[] bpattern = (byte[])listReply.get(1);
/* 151 */           T enpattern = (bpattern == null) ? null : encode(bpattern);
/* 152 */           onPSubscribe(enpattern, this.subscribedChannels);
/* 153 */         } else if (Arrays.equals(Protocol.ResponseKeyword.PUNSUBSCRIBE.getRaw(), resp)) {
/* 154 */           this.subscribedChannels = ((Long)listReply.get(2)).intValue();
/* 155 */           byte[] bpattern = (byte[])listReply.get(1);
/* 156 */           T enpattern = (bpattern == null) ? null : encode(bpattern);
/* 157 */           onPUnsubscribe(enpattern, this.subscribedChannels);
/* 158 */         } else if (Arrays.equals(Protocol.ResponseKeyword.PONG.getRaw(), resp)) {
/* 159 */           byte[] bpattern = (byte[])listReply.get(1);
/* 160 */           T enpattern = (bpattern == null) ? null : encode(bpattern);
/* 161 */           onPong(enpattern);
/*     */         } else {
/* 163 */           throw new JedisException("Unknown message type: " + firstObj);
/*     */         } 
/* 165 */       } else if (reply instanceof byte[]) {
/* 166 */         byte[] resp = (byte[])reply;
/* 167 */         if ("PONG".equals(SafeEncoder.encode(resp))) {
/* 168 */           onPong(null);
/*     */         } else {
/* 170 */           onPong(encode(resp));
/*     */         } 
/*     */       } else {
/* 173 */         throw new JedisException("Unknown message type: " + reply);
/*     */       } 
/* 175 */     } while (isSubscribed());
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisPubSubBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */