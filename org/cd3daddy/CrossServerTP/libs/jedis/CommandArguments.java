/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.RawableFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.RediSearchUtil;
/*     */ 
/*     */ 
/*     */ public class CommandArguments
/*     */   implements Iterable<Rawable>
/*     */ {
/*     */   private final ArrayList<Rawable> args;
/*     */   private boolean blocking;
/*     */   
/*     */   private CommandArguments() {
/*  20 */     throw new InstantiationError();
/*     */   }
/*     */   
/*     */   public CommandArguments(ProtocolCommand command) {
/*  24 */     this.args = new ArrayList<>();
/*  25 */     this.args.add(command);
/*     */   }
/*     */   
/*     */   public ProtocolCommand getCommand() {
/*  29 */     return (ProtocolCommand)this.args.get(0);
/*     */   }
/*     */   
/*     */   public CommandArguments add(Object arg) {
/*  33 */     if (arg == null)
/*  34 */       throw new IllegalArgumentException("null is not a valid argument."); 
/*  35 */     if (arg instanceof Rawable) {
/*  36 */       this.args.add((Rawable)arg);
/*  37 */     } else if (arg instanceof byte[]) {
/*  38 */       this.args.add(RawableFactory.from((byte[])arg));
/*  39 */     } else if (arg instanceof Integer) {
/*  40 */       this.args.add(RawableFactory.from(((Integer)arg).intValue()));
/*  41 */     } else if (arg instanceof Double) {
/*  42 */       this.args.add(RawableFactory.from(((Double)arg).doubleValue()));
/*  43 */     } else if (arg instanceof Boolean) {
/*  44 */       this.args.add(RawableFactory.from(((Boolean)arg).booleanValue() ? 1 : 0));
/*  45 */     } else if (arg instanceof float[]) {
/*  46 */       this.args.add(RawableFactory.from(RediSearchUtil.toByteArray((float[])arg)));
/*  47 */     } else if (arg instanceof String) {
/*  48 */       this.args.add(RawableFactory.from((String)arg));
/*  49 */     } else if (arg instanceof GeoCoordinate) {
/*  50 */       GeoCoordinate geo = (GeoCoordinate)arg;
/*  51 */       this.args.add(RawableFactory.from(geo.getLongitude() + "," + geo.getLatitude()));
/*     */     } else {
/*  53 */       this.args.add(RawableFactory.from(String.valueOf(arg)));
/*     */     } 
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public CommandArguments addObjects(Object... args) {
/*  59 */     for (Object arg : args) {
/*  60 */       add(arg);
/*     */     }
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public CommandArguments addObjects(Collection args) {
/*  66 */     args.forEach(arg -> add(arg));
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public CommandArguments key(Object key) {
/*  71 */     if (key instanceof Rawable) {
/*  72 */       Rawable raw = (Rawable)key;
/*  73 */       processKey(raw.getRaw());
/*  74 */       this.args.add(raw);
/*  75 */     } else if (key instanceof byte[]) {
/*  76 */       byte[] raw = (byte[])key;
/*  77 */       processKey(raw);
/*  78 */       this.args.add(RawableFactory.from(raw));
/*  79 */     } else if (key instanceof String) {
/*  80 */       String raw = (String)key;
/*  81 */       processKey(raw);
/*  82 */       this.args.add(RawableFactory.from(raw));
/*     */     } else {
/*  84 */       throw new IllegalArgumentException("\"" + key.toString() + "\" is not a valid argument.");
/*     */     } 
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public final CommandArguments keys(Object... keys) {
/*  90 */     for (Object key : keys) {
/*  91 */       key(key);
/*     */     }
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public final CommandArguments keys(Collection keys) {
/*  97 */     keys.forEach(key -> key(key));
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public final CommandArguments addParams(IParams params) {
/* 102 */     params.addParams(this);
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CommandArguments processKey(byte[] key) {
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   protected final CommandArguments processKeys(byte[]... keys) {
/* 112 */     for (byte[] key : keys) {
/* 113 */       processKey(key);
/*     */     }
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CommandArguments processKey(String key) {
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   protected final CommandArguments processKeys(String... keys) {
/* 124 */     for (String key : keys) {
/* 125 */       processKey(key);
/*     */     }
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public int size() {
/* 131 */     return this.args.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<Rawable> iterator() {
/* 136 */     return this.args.iterator();
/*     */   }
/*     */   
/*     */   public boolean isBlocking() {
/* 140 */     return this.blocking;
/*     */   }
/*     */   
/*     */   public CommandArguments blocking() {
/* 144 */     this.blocking = true;
/* 145 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\CommandArguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */