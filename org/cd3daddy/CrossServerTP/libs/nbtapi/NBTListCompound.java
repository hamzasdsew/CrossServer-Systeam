/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTListCompound
/*    */   extends NBTCompound
/*    */ {
/*    */   private NBTList<?> owner;
/*    */   private Object compound;
/*    */   
/*    */   protected NBTListCompound(NBTList<?> parent, Object obj) {
/* 18 */     super(null, null);
/* 19 */     this.owner = parent;
/* 20 */     this.compound = obj;
/*    */   }
/*    */   
/*    */   public NBTList<?> getListParent() {
/* 24 */     return this.owner;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isClosed() {
/* 29 */     return this.owner.getParent().isClosed();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isReadOnly() {
/* 34 */     return this.owner.getParent().isReadOnly();
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getCompound() {
/* 39 */     if (isClosed()) {
/* 40 */       throw new NbtApiException("Tried using closed NBT data!");
/*    */     }
/* 42 */     return this.compound;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setCompound(Object compound) {
/* 47 */     if (isClosed()) {
/* 48 */       throw new NbtApiException("Tried using closed NBT data!");
/*    */     }
/* 50 */     if (isReadOnly()) {
/* 51 */       throw new NbtApiException("Tried setting data in read only mode!");
/*    */     }
/* 53 */     this.compound = compound;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void saveCompound() {
/* 58 */     this.owner.save();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTListCompound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */