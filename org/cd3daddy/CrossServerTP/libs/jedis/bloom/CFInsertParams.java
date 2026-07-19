/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.bloom;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CFInsertParams
/*    */   implements IParams
/*    */ {
/*    */   private Long capacity;
/*    */   private boolean noCreate = false;
/*    */   
/*    */   public static CFInsertParams insertParams() {
/* 17 */     return new CFInsertParams();
/*    */   }
/*    */   
/*    */   public CFInsertParams capacity(long capacity) {
/* 21 */     this.capacity = Long.valueOf(capacity);
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public CFInsertParams noCreate() {
/* 26 */     this.noCreate = true;
/* 27 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 32 */     if (this.capacity != null) {
/* 33 */       args.add(RedisBloomProtocol.RedisBloomKeyword.CAPACITY).add(Protocol.toByteArray(this.capacity.longValue()));
/*    */     }
/* 35 */     if (this.noCreate)
/* 36 */       args.add(RedisBloomProtocol.RedisBloomKeyword.NOCREATE); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\CFInsertParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */