package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands.RedisBloomPipelineCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.graph.RedisGraphPipelineCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.json.commands.RedisJsonPipelineCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.search.RediSearchPipelineCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.RedisTimeSeriesPipelineCommands;

public interface RedisModulePipelineCommands extends RediSearchPipelineCommands, RedisJsonPipelineCommands, RedisTimeSeriesPipelineCommands, RedisBloomPipelineCommands, RedisGraphPipelineCommands {}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\RedisModulePipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */