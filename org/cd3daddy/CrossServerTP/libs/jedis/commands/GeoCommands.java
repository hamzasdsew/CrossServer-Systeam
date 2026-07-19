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

public interface GeoCommands {
  long geoadd(String paramString1, double paramDouble1, double paramDouble2, String paramString2);
  
  long geoadd(String paramString, Map<String, GeoCoordinate> paramMap);
  
  long geoadd(String paramString, GeoAddParams paramGeoAddParams, Map<String, GeoCoordinate> paramMap);
  
  Double geodist(String paramString1, String paramString2, String paramString3);
  
  Double geodist(String paramString1, String paramString2, String paramString3, GeoUnit paramGeoUnit);
  
  List<String> geohash(String paramString, String... paramVarArgs);
  
  List<GeoCoordinate> geopos(String paramString, String... paramVarArgs);
  
  List<GeoRadiusResponse> georadius(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadiusReadonly(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadius(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  List<GeoRadiusResponse> georadiusReadonly(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  List<GeoRadiusResponse> georadiusByMember(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadiusByMemberReadonly(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> georadiusByMember(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  List<GeoRadiusResponse> georadiusByMemberReadonly(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  long georadiusStore(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  long georadiusByMemberStore(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  List<GeoRadiusResponse> geosearch(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(String paramString, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(String paramString1, String paramString2, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(String paramString, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  List<GeoRadiusResponse> geosearch(String paramString, GeoSearchParam paramGeoSearchParam);
  
  long geosearchStore(String paramString1, String paramString2, String paramString3, double paramDouble, GeoUnit paramGeoUnit);
  
  long geosearchStore(String paramString1, String paramString2, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  long geosearchStore(String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  long geosearchStore(String paramString1, String paramString2, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  long geosearchStore(String paramString1, String paramString2, GeoSearchParam paramGeoSearchParam);
  
  long geosearchStoreStoreDist(String paramString1, String paramString2, GeoSearchParam paramGeoSearchParam);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\GeoCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */