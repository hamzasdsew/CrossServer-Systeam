/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JedisShardedPubSubBase<T>
/*     */ {
/*  13 */   private int subscribedChannels = 0;
/*     */   
/*     */   private volatile Connection client;
/*     */ 
/*     */   
/*     */   public void onSMessage(T channel, T message) {}
/*     */ 
/*     */   
/*     */   public void onSSubscribe(T channel, int subscribedChannels) {}
/*     */   
/*     */   public void onSUnsubscribe(T channel, int subscribedChannels) {}
/*     */   
/*     */   private void sendAndFlushCommand(Protocol.Command command, T... args) {
/*  26 */     if (this.client == null) {
/*  27 */       throw new JedisException(getClass() + " is not connected to a Connection.");
/*     */     }
/*  29 */     CommandArguments cargs = (new CommandArguments(command)).addObjects((Object[])args);
/*  30 */     this.client.sendCommand(cargs);
/*  31 */     this.client.flush();
/*     */   }
/*     */   
/*     */   public final void sunsubscribe() {
/*  35 */     sendAndFlushCommand(Protocol.Command.SUNSUBSCRIBE, (T[])new Object[0]);
/*     */   }
/*     */   
/*     */   public final void sunsubscribe(T... channels) {
/*  39 */     sendAndFlushCommand(Protocol.Command.SUNSUBSCRIBE, channels);
/*     */   }
/*     */   
/*     */   public final void ssubscribe(T... channels) {
/*  43 */     sendAndFlushCommand(Protocol.Command.SSUBSCRIBE, channels);
/*     */   }
/*     */   
/*     */   public final boolean isSubscribed() {
/*  47 */     return (this.subscribedChannels > 0);
/*     */   }
/*     */   
/*     */   public final int getSubscribedChannels() {
/*  51 */     return this.subscribedChannels;
/*     */   }
/*     */   
/*     */   public final void proceed(Connection client, T... channels) {
/*  55 */     this.client = client;
/*  56 */     this.client.setTimeoutInfinite();
/*     */     try {
/*  58 */       ssubscribe(channels);
/*  59 */       process();
/*     */     } finally {
/*  61 */       this.client.rollbackTimeout();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract T encode(byte[] paramArrayOfbyte);
/*     */ 
/*     */   
/*     */   private void process() {
/*     */     do {
/*  71 */       Object reply = this.client.getUnflushedObject();
/*     */       
/*  73 */       if (reply instanceof List) {
/*  74 */         List<Object> listReply = (List<Object>)reply;
/*  75 */         Object firstObj = listReply.get(0);
/*  76 */         if (!(firstObj instanceof byte[])) {
/*  77 */           throw new JedisException("Unknown message type: " + firstObj);
/*     */         }
/*  79 */         byte[] resp = (byte[])firstObj;
/*  80 */         if (Arrays.equals(Protocol.ResponseKeyword.SSUBSCRIBE.getRaw(), resp)) {
/*  81 */           this.subscribedChannels = ((Long)listReply.get(2)).intValue();
/*  82 */           byte[] bchannel = (byte[])listReply.get(1);
/*  83 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/*  84 */           onSSubscribe(enchannel, this.subscribedChannels);
/*  85 */         } else if (Arrays.equals(Protocol.ResponseKeyword.SUNSUBSCRIBE.getRaw(), resp)) {
/*  86 */           this.subscribedChannels = ((Long)listReply.get(2)).intValue();
/*  87 */           byte[] bchannel = (byte[])listReply.get(1);
/*  88 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/*  89 */           onSUnsubscribe(enchannel, this.subscribedChannels);
/*  90 */         } else if (Arrays.equals(Protocol.ResponseKeyword.SMESSAGE.getRaw(), resp)) {
/*  91 */           byte[] bchannel = (byte[])listReply.get(1);
/*  92 */           byte[] bmesg = (byte[])listReply.get(2);
/*  93 */           T enchannel = (bchannel == null) ? null : encode(bchannel);
/*  94 */           T enmesg = (bmesg == null) ? null : encode(bmesg);
/*  95 */           onSMessage(enchannel, enmesg);
/*     */         } else {
/*  97 */           throw new JedisException("Unknown message type: " + firstObj);
/*     */         } 
/*     */       } else {
/* 100 */         throw new JedisException("Unknown message type: " + reply);
/*     */       } 
/* 102 */     } while (isSubscribed());
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisShardedPubSubBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */