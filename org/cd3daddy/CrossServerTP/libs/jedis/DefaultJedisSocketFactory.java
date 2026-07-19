/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*     */ 
/*     */ public class DefaultJedisSocketFactory
/*     */   implements JedisSocketFactory
/*     */ {
/*  19 */   protected static final HostAndPort DEFAULT_HOST_AND_PORT = new HostAndPort("127.0.0.1", 6379);
/*     */ 
/*     */   
/*  22 */   private volatile HostAndPort hostAndPort = DEFAULT_HOST_AND_PORT;
/*  23 */   private int connectionTimeout = 2000;
/*  24 */   private int socketTimeout = 2000;
/*     */   private boolean ssl = false;
/*  26 */   private SSLSocketFactory sslSocketFactory = null;
/*  27 */   private SSLParameters sslParameters = null;
/*  28 */   private HostnameVerifier hostnameVerifier = null;
/*  29 */   private HostAndPortMapper hostAndPortMapper = null;
/*     */ 
/*     */   
/*     */   public DefaultJedisSocketFactory() {}
/*     */   
/*     */   public DefaultJedisSocketFactory(HostAndPort hostAndPort) {
/*  35 */     this(hostAndPort, null);
/*     */   }
/*     */   
/*     */   public DefaultJedisSocketFactory(JedisClientConfig config) {
/*  39 */     this(null, config);
/*     */   }
/*     */   
/*     */   public DefaultJedisSocketFactory(HostAndPort hostAndPort, JedisClientConfig config) {
/*  43 */     if (hostAndPort != null) {
/*  44 */       this.hostAndPort = hostAndPort;
/*     */     }
/*  46 */     if (config != null) {
/*  47 */       this.connectionTimeout = config.getConnectionTimeoutMillis();
/*  48 */       this.socketTimeout = config.getSocketTimeoutMillis();
/*  49 */       this.ssl = config.isSsl();
/*  50 */       this.sslSocketFactory = config.getSslSocketFactory();
/*  51 */       this.sslParameters = config.getSslParameters();
/*  52 */       this.hostnameVerifier = config.getHostnameVerifier();
/*  53 */       this.hostAndPortMapper = config.getHostAndPortMapper();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Socket connectToFirstSuccessfulHost(HostAndPort hostAndPort) throws Exception {
/*  58 */     List<InetAddress> hosts = Arrays.asList(InetAddress.getAllByName(hostAndPort.getHost()));
/*  59 */     if (hosts.size() > 1) {
/*  60 */       Collections.shuffle(hosts);
/*     */     }
/*     */     
/*  63 */     JedisConnectionException jce = new JedisConnectionException("Failed to connect to any host resolved for DNS name.");
/*  64 */     for (InetAddress host : hosts) {
/*     */       try {
/*  66 */         Socket socket = new Socket();
/*     */         
/*  68 */         socket.setReuseAddress(true);
/*  69 */         socket.setKeepAlive(true);
/*  70 */         socket.setTcpNoDelay(true);
/*  71 */         socket.setSoLinger(true, 0);
/*     */ 
/*     */ 
/*     */         
/*  75 */         socket.connect(new InetSocketAddress(host, hostAndPort.getPort()), this.connectionTimeout);
/*  76 */         return socket;
/*  77 */       } catch (Exception e) {
/*  78 */         jce.addSuppressed(e);
/*     */       } 
/*     */     } 
/*  81 */     throw jce;
/*     */   }
/*     */ 
/*     */   
/*     */   public Socket createSocket() throws JedisConnectionException {
/*  86 */     Socket socket = null;
/*     */     try {
/*  88 */       HostAndPort _hostAndPort = getSocketHostAndPort();
/*  89 */       socket = connectToFirstSuccessfulHost(_hostAndPort);
/*  90 */       socket.setSoTimeout(this.socketTimeout);
/*     */       
/*  92 */       if (this.ssl) {
/*  93 */         SSLSocketFactory _sslSocketFactory = this.sslSocketFactory;
/*  94 */         if (null == _sslSocketFactory) {
/*  95 */           _sslSocketFactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
/*     */         }
/*  97 */         socket = _sslSocketFactory.createSocket(socket, _hostAndPort.getHost(), _hostAndPort.getPort(), true);
/*     */         
/*  99 */         if (null != this.sslParameters) {
/* 100 */           ((SSLSocket)socket).setSSLParameters(this.sslParameters);
/*     */         }
/*     */         
/* 103 */         if (null != this.hostnameVerifier && 
/* 104 */           !this.hostnameVerifier.verify(_hostAndPort.getHost(), ((SSLSocket)socket).getSession())) {
/* 105 */           String message = String.format("The connection to '%s' failed ssl/tls hostname verification.", new Object[] { _hostAndPort
/* 106 */                 .getHost() });
/* 107 */           throw new JedisConnectionException(message);
/*     */         } 
/*     */       } 
/*     */       
/* 111 */       return socket;
/*     */     }
/* 113 */     catch (Exception ex) {
/* 114 */       IOUtils.closeQuietly(socket);
/* 115 */       if (ex instanceof JedisConnectionException) {
/* 116 */         throw (JedisConnectionException)ex;
/*     */       }
/* 118 */       throw new JedisConnectionException("Failed to create socket.", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateHostAndPort(HostAndPort hostAndPort) {
/* 124 */     this.hostAndPort = hostAndPort;
/*     */   }
/*     */   
/*     */   public HostAndPort getHostAndPort() {
/* 128 */     return this.hostAndPort;
/*     */   }
/*     */   
/*     */   protected HostAndPort getSocketHostAndPort() {
/* 132 */     HostAndPortMapper mapper = this.hostAndPortMapper;
/* 133 */     HostAndPort hap = this.hostAndPort;
/* 134 */     if (mapper != null) {
/* 135 */       HostAndPort mapped = mapper.getHostAndPort(hap);
/* 136 */       if (mapped != null) {
/* 137 */         return mapped;
/*     */       }
/*     */     } 
/* 140 */     return hap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     return "DefaultJedisSocketFactory{" + this.hostAndPort.toString() + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\DefaultJedisSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */