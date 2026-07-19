/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class ZParams
/*    */   implements IParams
/*    */ {
/*    */   public enum Aggregate
/*    */     implements Rawable {
/* 15 */     SUM, MIN, MAX;
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     Aggregate() {
/* 20 */       this.raw = SafeEncoder.encode(name());
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 25 */       return this.raw;
/*    */     }
/*    */   }
/*    */   
/* 29 */   private final List<Object> params = new ArrayList();
/*    */   
/*    */   public ZParams weights(double... weights) {
/* 32 */     this.params.add(Protocol.Keyword.WEIGHTS);
/* 33 */     for (double weight : weights) {
/* 34 */       this.params.add(Double.valueOf(weight));
/*    */     }
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public ZParams aggregate(Aggregate aggregate) {
/* 40 */     this.params.add(Protocol.Keyword.AGGREGATE);
/* 41 */     this.params.add(aggregate);
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 47 */     args.addObjects(this.params);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ZParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */