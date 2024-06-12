1. mybatis-plus从3.5.5升级至3.5.6，使用ServiceImpl的saveBatch方法报错demo，
   - 执行单元测试代码：com.magictan.service.TestServiceTest.test
   - Caused by: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for com.magictan.mapper.TestMapper.insert
2. 项目中存在多个sqlSessionFactory，升级3.5.6后，TestService获取到了错误的sqlSessionFactory
3. 比对3.5.5和3.5.6版本代码差异，发现差异来自3.5.5和3.5.6版本ServiceImpl.getSqlSessionFactory的这行代码：
```java
// 3.5.6版本代码 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.getSqlSessionFactory
@SuppressWarnings({"rawtypes", "deprecation"})
protected SqlSessionFactory getSqlSessionFactory() {
   if (this.sqlSessionFactory == null) {
      synchronized (this) {
         if (this.sqlSessionFactory == null) {
            Object target = this.baseMapper;
            // 这个检查目前看着来说基本上可以不用判断Aop是不是存在了.
            if (com.baomidou.mybatisplus.extension.toolkit.AopUtils.isLoadSpringAop()) {
               while (AopUtils.isAopProxy(target)) {
                  target = AopProxyUtils.getSingletonTarget(target);
               }
            }
            if (target instanceof MybatisMapperProxy) { // 3.5.6 使用 instanceof 判断，最后TestService走到else分支
               MybatisMapperProxy mybatisMapperProxy = (MybatisMapperProxy) Proxy.getInvocationHandler(target);
               SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) mybatisMapperProxy.getSqlSession();
               this.sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
            } else {
               this.sqlSessionFactory = GlobalConfigUtils.currentSessionFactory(this.entityClass);
            }
         }
      }
   }
   return this.sqlSessionFactory;
}

// 3.5.5版本代码 com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.getSqlSessionFactory
@SuppressWarnings({"rawtypes", "deprecation"})
protected SqlSessionFactory getSqlSessionFactory() {
   if (this.sqlSessionFactory == null) {
      synchronized (this) {
         if (this.sqlSessionFactory == null) {
            Object target = this.baseMapper;
            // 这个检查目前看着来说基本上可以不用判断Aop是不是存在了.
            if (com.baomidou.mybatisplus.extension.toolkit.AopUtils.isLoadSpringAop()) {
               if (AopUtils.isAopProxy(this.baseMapper)) {
                  target = AopProxyUtils.getSingletonTarget(this.baseMapper);
               }
            }
            if (target != null) { // 3.5.5 使用 != null 判断，最后TestService走到if分支
               MybatisMapperProxy mybatisMapperProxy = (MybatisMapperProxy) Proxy.getInvocationHandler(target);
               SqlSessionTemplate sqlSessionTemplate = (SqlSessionTemplate) mybatisMapperProxy.getSqlSession();
               this.sqlSessionFactory = sqlSessionTemplate.getSqlSessionFactory();
            } else {
               this.sqlSessionFactory = GlobalConfigUtils.currentSessionFactory(this.entityClass);
            }
         }
      }
   }
   return this.sqlSessionFactory;
}
```

