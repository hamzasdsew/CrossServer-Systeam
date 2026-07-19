package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands.RedisBloomCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.gears.RedisGearsCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.graph.RedisGraphCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.json.commands.RedisJsonCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.search.RediSearchCommands;
import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.RedisTimeSeriesCommands;

public interface RedisModuleCommands extends RediSearchCommands, RedisJsonCommands, RedisTimeSeriesCommands, RedisBloomCommands, RedisGraphCommands, RedisGearsCommands {}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\RedisModuleCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */