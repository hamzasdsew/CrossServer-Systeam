/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ public class StreamEntryID implements Comparable<StreamEntryID>, Serializable {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private long time;
/*     */   private long sequence;
/*     */   
/*     */   public StreamEntryID() {
/*  15 */     this(0L, 0L);
/*     */   }
/*     */   
/*     */   public StreamEntryID(byte[] id) {
/*  19 */     this(SafeEncoder.encode(id));
/*     */   }
/*     */   
/*     */   public StreamEntryID(String id) {
/*  23 */     String[] split = id.split("-");
/*  24 */     this.time = Long.parseLong(split[0]);
/*  25 */     this.sequence = Long.parseLong(split[1]);
/*     */   }
/*     */   
/*     */   public StreamEntryID(long time) {
/*  29 */     this(time, 0L);
/*     */   }
/*     */   
/*     */   public StreamEntryID(long time, long sequence) {
/*  33 */     this.time = time;
/*  34 */     this.sequence = sequence;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  39 */     return this.time + "-" + this.sequence;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  44 */     if (this == obj) return true; 
/*  45 */     if (obj == null) return false; 
/*  46 */     if (getClass() != obj.getClass()) return false; 
/*  47 */     StreamEntryID other = (StreamEntryID)obj;
/*  48 */     return (this.time == other.time && this.sequence == other.sequence);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  53 */     return toString().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(StreamEntryID other) {
/*  58 */     int timeCompare = Long.compare(this.time, other.time);
/*  59 */     return (timeCompare != 0) ? timeCompare : Long.compare(this.sequence, other.sequence);
/*     */   }
/*     */   
/*     */   public long getTime() {
/*  63 */     return this.time;
/*     */   }
/*     */   
/*     */   public long getSequence() {
/*  67 */     return this.sequence;
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/*  71 */     out.writeLong(this.time);
/*  72 */     out.writeLong(this.sequence);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/*  76 */     this.time = in.readLong();
/*  77 */     this.sequence = in.readLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final StreamEntryID NEW_ENTRY = new StreamEntryID()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */       
/*     */       public String toString() {
/*  93 */         return "*";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   public static final StreamEntryID LAST_ENTRY = new StreamEntryID()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */       
/*     */       public String toString() {
/* 110 */         return "$";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   public static final StreamEntryID UNRECEIVED_ENTRY = new StreamEntryID()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */       
/*     */       public String toString() {
/* 125 */         return ">";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 132 */   public static final StreamEntryID MINIMUM_ID = new StreamEntryID()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */       
/*     */       public String toString() {
/* 138 */         return "-";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   public static final StreamEntryID MAXIMUM_ID = new StreamEntryID()
/*     */     {
/*     */       private static final long serialVersionUID = 1L;
/*     */ 
/*     */       
/*     */       public String toString() {
/* 151 */         return "+";
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\StreamEntryID.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */