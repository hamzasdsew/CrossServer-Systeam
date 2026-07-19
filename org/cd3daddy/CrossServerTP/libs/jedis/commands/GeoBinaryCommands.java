package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.GeoCoordinate;
import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoAddParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusParam;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusStoreParam;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoSearchParam;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;

public interface GeoBinaryCommands {
  long geoadd(byte[] paramArrayOfbyte1, double paramDouble1, double paramDouble2, byte[] paramArrayOfbyte2);
  
  long geoadd(byte[] paramArrayOfbyte, Map<byte[], GeoCoordinate> paramMap);
  
  long geoadd(byte[] paramArrayOfbyte, GeoAddParams paramGeoAddParams, Map<byte[], GeoCoordinate> paramMap);
  
  Double geodist(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
  
  Double geodist(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, GeoUnit paramGeoUnit);
  
  List<byte[]> geohash(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  List<GeoCoordinate> geopos(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  List<GeoRadiusResponse> georadius(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadiusReadonly(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadius(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  List<GeoRadiusResponse> georadiusReadonly(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  List<GeoRadiusResponse> georadiusByMember(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadiusByMember(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  long georadiusStore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  long georadiusByMemberStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  List<GeoRadiusResponse> geosearch(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(byte[] paramArrayOfbyte, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(byte[] paramArrayOfbyte, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(byte[] paramArrayOfbyte, GeoSearchParam paramGeoSearchParam);
  
  long geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, double paramDouble, GeoUnit paramGeoUnit);
  
  long geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  long geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  long geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  long geosearchStore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoSearchParam paramGeoSearchParam);
  
  long geosearchStoreStoreDist(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, GeoSearchParam paramGeoSearchParam);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\GeoBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */