/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum NBTType
/*    */ {
/* 11 */   NBTTagEnd(0, ""), NBTTagByte(1, "BYTE"), NBTTagShort(2, "SHORT"), NBTTagInt(3, "INT"), NBTTagLong(4, "LONG"), NBTTagFloat(5, "FLOAT"), NBTTagDouble(6, "DOUBLE"),
/* 12 */   NBTTagByteArray(7, "BYTE[]"), NBTTagString(8, "STRING"), NBTTagList(9, "LIST"), NBTTagCompound(10, "COMPOUND"), NBTTagIntArray(11, "INT[]"), NBTTagLongArray(12, "LONG[]"); private final int id;
/*    */   
/*    */   NBTType(int i, String name) {
/* 15 */     this.id = i;
/* 16 */     this.name = name;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private final String name;
/*    */ 
/*    */ 
/*    */   
/*    */   public int getId() {
/* 26 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static NBTType fromName(String name) {
/* 48 */     for (NBTType t : values()) {
/* 49 */       if (t.getName().equals(name))
/* 50 */         return t; 
/* 51 */     }  return NBTTagEnd;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */