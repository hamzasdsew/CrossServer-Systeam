package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.GeoCoordinate;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoAddParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusParam;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusStoreParam;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoSearchParam;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;

public interface GeoPipelineBinaryCommands {
  Response<Long> geoadd(byte[] paramArrayOfbyte1, double paramDouble1, double paramDouble2, byte[] paramArrayOfbyte2);
  
  Response<Long> geoadd(byte[] paramArrayOfbyte, Map<byte[], GeoCoordinate> paramMap);
  
  Response<Long> geoadd(byte[] paramArrayOfbyte, GeoAddParams paramGeoAddParams, Map<byte[], GeoCoordinate> paramMap);
  
  Response<Double> geodist(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
  
  Response<Double> geodist(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, GeoUnit paramGeoUnit);
  
  Response<List<byte[]>> geohash(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<List<GeoCoordinate>> geopos(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<List<GeoRadiusResponse>> georadius(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadiusReadonly(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadius(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<List<GeoRadiusResponse>> georadiusReadonly(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<List<GeoRadiusResponse>> georadiusByMember(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadiusByMember(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<Long> georadiusStore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  Response<Long> georadiusByMemberStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  Response<List<GeoRadiusResponse>> geosearch(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(byte[] paramArrayOfbyte, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(byte[] paramArrayOfbyte, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(byte[] paramArrayOfbyte, GeoSearchParam paramGeoSearchParam);
  
  Response<Long> geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoSearchParam paramGeoSearchParam);
  
  Response<Long> geosearchStoreStoreDist(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoSearchParam paramGeoSearchParam);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\GeoPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */