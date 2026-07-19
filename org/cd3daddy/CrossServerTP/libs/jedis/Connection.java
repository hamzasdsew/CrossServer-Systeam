/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientAttributeOption;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisValidationException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.RedisInputStream;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.RedisOutputStream;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Connection
/*     */   implements Closeable
/*     */ {
/*     */   private ConnectionPool memberOf;
/*     */   private RedisProtocol protocol;
/*     */   private final JedisSocketFactory socketFactory;
/*     */   private Socket socket;
/*     */   private RedisOutputStream outputStream;
/*     */   private RedisInputStream inputStream;
/*  37 */   private int soTimeout = 0;
/*  38 */   private int infiniteSoTimeout = 0;
/*     */   private boolean broken = false;
/*     */   
/*     */   public Connection() {
/*  42 */     this("127.0.0.1", 6379);
/*     */   }
/*     */   
/*     */   public Connection(String host, int port) {
/*  46 */     this(new HostAndPort(host, port));
/*     */   }
/*     */   
/*     */   public Connection(HostAndPort hostAndPort) {
/*  50 */     this(new DefaultJedisSocketFactory(hostAndPort));
/*     */   }
/*     */   
/*     */   public Connection(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/*  54 */     this(new DefaultJedisSocketFactory(hostAndPort, clientConfig));
/*  55 */     this.infiniteSoTimeout = clientConfig.getBlockingSocketTimeoutMillis();
/*  56 */     initializeFromClientConfig(clientConfig);
/*     */   }
/*     */   
/*     */   public Connection(JedisSocketFactory socketFactory) {
/*  60 */     this.socketFactory = socketFactory;
/*     */   }
/*     */   
/*     */   public Connection(JedisSocketFactory socketFactory, JedisClientConfig clientConfig) {
/*  64 */     this.socketFactory = socketFactory;
/*  65 */     this.soTimeout = clientConfig.getSocketTimeoutMillis();
/*  66 */     this.infiniteSoTimeout = clientConfig.getBlockingSocketTimeoutMillis();
/*  67 */     initializeFromClientConfig(clientConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     return "Connection{" + this.socketFactory + "}";
/*     */   }
/*     */   
/*     */   public final RedisProtocol getRedisProtocol() {
/*  76 */     return this.protocol;
/*     */   }
/*     */   
/*     */   public final void setHandlingPool(ConnectionPool pool) {
/*  80 */     this.memberOf = pool;
/*     */   }
/*     */   
/*     */   final HostAndPort getHostAndPort() {
/*  84 */     return ((DefaultJedisSocketFactory)this.socketFactory).getHostAndPort();
/*     */   }
/*     */   
/*     */   public int getSoTimeout() {
/*  88 */     return this.soTimeout;
/*     */   }
/*     */   
/*     */   public void setSoTimeout(int soTimeout) {
/*  92 */     this.soTimeout = soTimeout;
/*  93 */     if (this.socket != null) {
/*     */       try {
/*  95 */         this.socket.setSoTimeout(soTimeout);
/*  96 */       } catch (SocketException ex) {
/*  97 */         this.broken = true;
/*  98 */         throw new JedisConnectionException(ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTimeoutInfinite() {
/*     */     try {
/* 105 */       if (!isConnected()) {
/* 106 */         connect();
/*     */       }
/* 108 */       this.socket.setSoTimeout(this.infiniteSoTimeout);
/* 109 */     } catch (SocketException ex) {
/* 110 */       this.broken = true;
/* 111 */       throw new JedisConnectionException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rollbackTimeout() {
/*     */     try {
/* 117 */       this.socket.setSoTimeout(this.soTimeout);
/* 118 */     } catch (SocketException ex) {
/* 119 */       this.broken = true;
/* 120 */       throw new JedisConnectionException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object executeCommand(ProtocolCommand cmd) {
/* 125 */     return executeCommand(new CommandArguments(cmd));
/*     */   }
/*     */   
/*     */   public Object executeCommand(CommandArguments args) {
/* 129 */     sendCommand(args);
/* 130 */     return getOne();
/*     */   }
/*     */   
/*     */   public <T> T executeCommand(CommandObject<T> commandObject) {
/* 134 */     CommandArguments args = commandObject.getArguments();
/* 135 */     sendCommand(args);
/* 136 */     if (!args.isBlocking()) {
/* 137 */       return commandObject.getBuilder().build(getOne());
/*     */     }
/*     */     try {
/* 140 */       setTimeoutInfinite();
/* 141 */       return (T)commandObject.getBuilder().build(getOne());
/*     */     } finally {
/* 143 */       rollbackTimeout();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sendCommand(ProtocolCommand cmd) {
/* 149 */     sendCommand(new CommandArguments(cmd));
/*     */   }
/*     */   
/*     */   public void sendCommand(ProtocolCommand cmd, Rawable keyword) {
/* 153 */     sendCommand((new CommandArguments(cmd)).add(keyword));
/*     */   }
/*     */   
/*     */   public void sendCommand(ProtocolCommand cmd, String... args) {
/* 157 */     sendCommand((new CommandArguments(cmd)).addObjects((Object[])args));
/*     */   }
/*     */   
/*     */   public void sendCommand(ProtocolCommand cmd, byte[]... args) {
/* 161 */     sendCommand((new CommandArguments(cmd)).addObjects((Object[])args));
/*     */   }
/*     */   
/*     */   public void sendCommand(CommandArguments args) {
/*     */     try {
/* 166 */       connect();
/* 167 */       Protocol.sendCommand(this.outputStream, args);
/* 168 */     } catch (JedisConnectionException ex) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/* 174 */         String errorMessage = Protocol.readErrorLineIfPossible(this.inputStream);
/* 175 */         if (errorMessage != null && errorMessage.length() > 0) {
/* 176 */           ex = new JedisConnectionException(errorMessage, ex.getCause());
/*     */         }
/* 178 */       } catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       this.broken = true;
/* 187 */       throw ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void connect() throws JedisConnectionException {
/* 192 */     if (!isConnected()) {
/*     */       try {
/* 194 */         this.socket = this.socketFactory.createSocket();
/* 195 */         this.soTimeout = this.socket.getSoTimeout();
/*     */         
/* 197 */         this.outputStream = new RedisOutputStream(this.socket.getOutputStream());
/* 198 */         this.inputStream = new RedisInputStream(this.socket.getInputStream());
/*     */         
/* 200 */         this.broken = false;
/*     */       }
/* 202 */       catch (JedisConnectionException jce) {
/*     */         
/* 204 */         setBroken();
/* 205 */         throw jce;
/*     */       }
/* 207 */       catch (IOException ioe) {
/*     */         
/* 209 */         setBroken();
/* 210 */         throw new JedisConnectionException("Failed to create input/output stream", ioe);
/*     */       }
/*     */       finally {
/*     */         
/* 214 */         if (this.broken) {
/* 215 */           IOUtils.closeQuietly(this.socket);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 223 */     if (this.memberOf != null) {
/* 224 */       ConnectionPool pool = this.memberOf;
/* 225 */       this.memberOf = null;
/* 226 */       if (isBroken()) {
/* 227 */         pool.returnBrokenResource(this);
/*     */       } else {
/* 229 */         pool.returnResource(this);
/*     */       } 
/*     */     } else {
/* 232 */       disconnect();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect() {
/* 240 */     if (isConnected()) {
/*     */       try {
/* 242 */         this.outputStream.flush();
/* 243 */         this.socket.close();
/* 244 */       } catch (IOException ex) {
/* 245 */         throw new JedisConnectionException(ex);
/*     */       } finally {
/* 247 */         IOUtils.closeQuietly(this.socket);
/* 248 */         setBroken();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isConnected() {
/* 254 */     return (this.socket != null && this.socket.isBound() && !this.socket.isClosed() && this.socket.isConnected() && 
/* 255 */       !this.socket.isInputShutdown() && !this.socket.isOutputShutdown());
/*     */   }
/*     */   
/*     */   public boolean isBroken() {
/* 259 */     return this.broken;
/*     */   }
/*     */   
/*     */   public void setBroken() {
/* 263 */     this.broken = true;
/*     */   }
/*     */   
/*     */   public String getStatusCodeReply() {
/* 267 */     flush();
/* 268 */     byte[] resp = (byte[])readProtocolWithCheckingBroken();
/* 269 */     if (null == resp) {
/* 270 */       return null;
/*     */     }
/* 272 */     return SafeEncoder.encode(resp);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBulkReply() {
/* 277 */     byte[] result = getBinaryBulkReply();
/* 278 */     if (null != result) {
/* 279 */       return SafeEncoder.encode(result);
/*     */     }
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBinaryBulkReply() {
/* 286 */     flush();
/* 287 */     return (byte[])readProtocolWithCheckingBroken();
/*     */   }
/*     */   
/*     */   public Long getIntegerReply() {
/* 291 */     flush();
/* 292 */     return (Long)readProtocolWithCheckingBroken();
/*     */   }
/*     */   
/*     */   public List<String> getMultiBulkReply() {
/* 296 */     return BuilderFactory.STRING_LIST.build(getBinaryMultiBulkReply());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<byte[]> getBinaryMultiBulkReply() {
/* 301 */     flush();
/* 302 */     return (List<byte[]>)readProtocolWithCheckingBroken();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<Object> getUnflushedObjectMultiBulkReply() {
/* 311 */     return (List<Object>)readProtocolWithCheckingBroken();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getUnflushedObject() {
/* 316 */     return readProtocolWithCheckingBroken();
/*     */   }
/*     */   
/*     */   public List<Object> getObjectMultiBulkReply() {
/* 320 */     flush();
/* 321 */     return (List<Object>)readProtocolWithCheckingBroken();
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Long> getIntegerMultiBulkReply() {
/* 326 */     flush();
/* 327 */     return (List<Long>)readProtocolWithCheckingBroken();
/*     */   }
/*     */   
/*     */   public Object getOne() {
/* 331 */     flush();
/* 332 */     return readProtocolWithCheckingBroken();
/*     */   }
/*     */   
/*     */   protected void flush() {
/*     */     try {
/* 337 */       this.outputStream.flush();
/* 338 */     } catch (IOException ex) {
/* 339 */       this.broken = true;
/* 340 */       throw new JedisConnectionException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Object readProtocolWithCheckingBroken() {
/* 345 */     if (this.broken) {
/* 346 */       throw new JedisConnectionException("Attempting to read from a broken connection");
/*     */     }
/*     */     
/*     */     try {
/* 350 */       return Protocol.read(this.inputStream);
/*     */ 
/*     */     
/*     */     }
/* 354 */     catch (JedisConnectionException exc) {
/* 355 */       this.broken = true;
/* 356 */       throw exc;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<Object> getMany(int count) {
/* 361 */     flush();
/* 362 */     List<Object> responses = new ArrayList(count);
/* 363 */     for (int i = 0; i < count; i++) {
/*     */       try {
/* 365 */         responses.add(readProtocolWithCheckingBroken());
/* 366 */       } catch (JedisDataException e) {
/* 367 */         responses.add(e);
/*     */       } 
/*     */     } 
/* 370 */     return responses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean validateClientInfo(String info) {
/* 380 */     for (int i = 0; i < info.length(); i++) {
/* 381 */       char c = info.charAt(i);
/* 382 */       if (c < '!' || c > '~') {
/* 383 */         throw new JedisValidationException("client info cannot contain spaces, newlines or special characters.");
/*     */       }
/*     */     } 
/*     */     
/* 387 */     return true;
/*     */   }
/*     */   
/*     */   private void initializeFromClientConfig(JedisClientConfig config) {
/*     */     try {
/* 392 */       connect();
/*     */       
/* 394 */       this.protocol = config.getRedisProtocol();
/*     */       
/* 396 */       Supplier<RedisCredentials> credentialsProvider = config.getCredentialsProvider();
/* 397 */       if (credentialsProvider instanceof RedisCredentialsProvider) {
/* 398 */         RedisCredentialsProvider redisCredentialsProvider = (RedisCredentialsProvider)credentialsProvider;
/*     */         try {
/* 400 */           redisCredentialsProvider.prepare();
/* 401 */           helloOrAuth(this.protocol, redisCredentialsProvider.get());
/*     */         } finally {
/* 403 */           redisCredentialsProvider.cleanUp();
/*     */         } 
/*     */       } else {
/* 406 */         helloOrAuth(this.protocol, (credentialsProvider != null) ? credentialsProvider.get() : new DefaultRedisCredentials(config
/* 407 */               .getUser(), config.getPassword()));
/*     */       } 
/*     */       
/* 410 */       List<CommandArguments> fireAndForgetMsg = new ArrayList<>();
/*     */       
/* 412 */       String clientName = config.getClientName();
/* 413 */       if (clientName != null && validateClientInfo(clientName)) {
/* 414 */         fireAndForgetMsg.add((new CommandArguments(Protocol.Command.CLIENT)).add(Protocol.Keyword.SETNAME).add(clientName));
/*     */       }
/*     */       
/* 417 */       ClientSetInfoConfig setInfoConfig = config.getClientSetInfoConfig();
/* 418 */       if (setInfoConfig == null) setInfoConfig = ClientSetInfoConfig.DEFAULT;
/*     */       
/* 420 */       if (!setInfoConfig.isDisabled()) {
/* 421 */         String libName = JedisMetaInfo.getArtifactId();
/* 422 */         if (libName != null && validateClientInfo(libName)) {
/* 423 */           String libNameSuffix = setInfoConfig.getLibNameSuffix();
/* 424 */           if (libNameSuffix != null) {
/* 425 */             libName = libName + '(' + libNameSuffix + ')';
/*     */           }
/* 427 */           fireAndForgetMsg.add((new CommandArguments(Protocol.Command.CLIENT)).add(Protocol.Keyword.SETINFO)
/* 428 */               .add(ClientAttributeOption.LIB_NAME.getRaw()).add(libName));
/*     */         } 
/*     */         
/* 431 */         String libVersion = JedisMetaInfo.getVersion();
/* 432 */         if (libVersion != null && validateClientInfo(libVersion)) {
/* 433 */           fireAndForgetMsg.add((new CommandArguments(Protocol.Command.CLIENT)).add(Protocol.Keyword.SETINFO)
/* 434 */               .add(ClientAttributeOption.LIB_VER.getRaw()).add(libVersion));
/*     */         }
/*     */       } 
/*     */       
/* 438 */       for (CommandArguments arg : fireAndForgetMsg) {
/* 439 */         sendCommand(arg);
/*     */       }
/* 441 */       getMany(fireAndForgetMsg.size());
/*     */       
/* 443 */       int dbIndex = config.getDatabase();
/* 444 */       if (dbIndex > 0) {
/* 445 */         select(dbIndex);
/*     */       }
/*     */     }
/* 448 */     catch (JedisException je) {
/*     */       try {
/* 450 */         disconnect();
/* 451 */       } catch (Exception exception) {}
/*     */ 
/*     */       
/* 454 */       throw je;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void helloOrAuth(RedisProtocol protocol, RedisCredentials credentials) {
/* 460 */     if (credentials == null || credentials.getPassword() == null) {
/* 461 */       if (protocol != null) {
/* 462 */         sendCommand(Protocol.Command.HELLO, new byte[][] { SafeEncoder.encode(protocol.version()) });
/* 463 */         getOne();
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 469 */     ByteBuffer passBuf = Protocol.CHARSET.encode(CharBuffer.wrap(credentials.getPassword()));
/* 470 */     byte[] rawPass = Arrays.copyOfRange(passBuf.array(), passBuf.position(), passBuf.limit());
/* 471 */     Arrays.fill(passBuf.array(), (byte)0);
/*     */ 
/*     */     
/*     */     try {
/* 475 */       if (protocol != null) {
/* 476 */         if (credentials.getUser() != null) {
/* 477 */           sendCommand(Protocol.Command.HELLO, new byte[][] { SafeEncoder.encode(protocol.version()), Protocol.Keyword.AUTH
/* 478 */                 .getRaw(), SafeEncoder.encode(credentials.getUser()), rawPass });
/* 479 */           getOne();
/*     */         } else {
/* 481 */           sendCommand(Protocol.Command.AUTH, new byte[][] { rawPass });
/* 482 */           getStatusCodeReply();
/* 483 */           sendCommand(Protocol.Command.HELLO, new byte[][] { SafeEncoder.encode(protocol.version()) });
/* 484 */           getOne();
/*     */         } 
/*     */       } else {
/* 487 */         if (credentials.getUser() != null) {
/* 488 */           sendCommand(Protocol.Command.AUTH, new byte[][] { SafeEncoder.encode(credentials.getUser()), rawPass });
/*     */         } else {
/* 490 */           sendCommand(Protocol.Command.AUTH, new byte[][] { rawPass });
/*     */         } 
/* 492 */         getStatusCodeReply();
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 497 */       Arrays.fill(rawPass, (byte)0);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String select(int index) {
/* 505 */     sendCommand(Protocol.Command.SELECT, new byte[][] { Protocol.toByteArray(index) });
/* 506 */     return getStatusCodeReply();
/*     */   }
/*     */   
/*     */   public boolean ping() {
/* 510 */     sendCommand(Protocol.Command.PING);
/* 511 */     String status = getStatusCodeReply();
/* 512 */     if (!"PONG".equals(status)) {
/* 513 */       throw new JedisException(status);
/*     */     }
/* 515 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */