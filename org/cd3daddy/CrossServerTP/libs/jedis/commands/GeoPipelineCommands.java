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

public interface GeoPipelineCommands {
  Response<Long> geoadd(String paramString1, double paramDouble1, double paramDouble2, String paramString2);
  
  Response<Long> geoadd(String paramString, Map<String, GeoCoordinate> paramMap);
  
  Response<Long> geoadd(String paramString, GeoAddParams paramGeoAddParams, Map<String, GeoCoordinate> paramMap);
  
  Response<Double> geodist(String paramString1, String paramString2, String paramString3);
  
  Response<Double> geodist(String paramString1, String paramString2, String paramString3, GeoUnit paramGeoUnit);
  
  Response<List<String>> geohash(String paramString, String... paramVarArgs);
  
  Response<List<GeoCoordinate>> geopos(String paramString, String... paramVarArgs);
  
  Response<List<GeoRadiusResponse>> georadius(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadiusReadonly(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadius(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<List<GeoRadiusResponse>> georadiusReadonly(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<List<GeoRadiusResponse>> georadiusByMember(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> georadiusByMember(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam);
  
  Response<Long> georadiusStore(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  Response<Long> georadiusByMemberStore(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit, GeoRadiusParam paramGeoRadiusParam, GeoRadiusStoreParam paramGeoRadiusStoreParam);
  
  Response<List<GeoRadiusResponse>> geosearch(String paramString1, String paramString2, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(String paramString, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(String paramString1, String paramString2, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(String paramString, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<List<GeoRadiusResponse>> geosearch(String paramString, GeoSearchParam paramGeoSearchParam);
  
  Response<Long> geosearchStore(String paramString1, String paramString2, String paramString3, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(String paramString1, String paramString2, GeoCoordinate paramGeoCoordinate, double paramDouble, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(String paramString1, String paramString2, String paramString3, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(String paramString1, String paramString2, GeoCoordinate paramGeoCoordinate, double paramDouble1, double paramDouble2, GeoUnit paramGeoUnit);
  
  Response<Long> geosearchStore(String paramString1, String paramString2, GeoSearchParam paramGeoSearchParam);
  
  Response<Long> geosearchStoreStoreDist(String paramString1, String paramString2, GeoSearchParam paramGeoSearchParam);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\GeoPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */