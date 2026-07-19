/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTIntArrayList
/*    */   extends NBTList<int[]>
/*    */ {
/*    */   private final NBTContainer tmpContainer;
/*    */   
/*    */   protected NBTIntArrayList(NBTCompound owner, String name, NBTType type, Object list) {
/* 20 */     super(owner, name, type, list);
/* 21 */     this.tmpContainer = new NBTContainer();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object asTag(int[] object) {
/*    */     try {
/* 27 */       Constructor<?> con = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(new Class[] { int[].class });
/* 28 */       con.setAccessible(true);
/* 29 */       return con.newInstance(new Object[] { object });
/* 30 */     } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException e) {
/*    */       
/* 32 */       throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] get(int index) {
/*    */     try {
/* 39 */       Object obj = ReflectionMethod.LIST_GET.run(this.listObject, new Object[] { Integer.valueOf(index) });
/* 40 */       ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), new Object[] { "tmp", obj });
/* 41 */       int[] val = this.tmpContainer.getIntArray("tmp");
/* 42 */       this.tmpContainer.removeKey("tmp");
/* 43 */       return val;
/* 44 */     } catch (NumberFormatException nf) {
/* 45 */       return null;
/* 46 */     } catch (Exception ex) {
/* 47 */       throw new NbtApiException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTIntArrayList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */