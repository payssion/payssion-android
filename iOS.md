# PayssionSDK
## 导入代码
##### 1. 将文件`PayssionSDK.framework`拷贝到项目文件夹下，并导入到项目工程中。   
在Build Phases选项卡的Link Binary With Libraries中，增加以下依赖：

![alt text](/libs/librarie.png "lib")

##### 2. 在需要调用PayssionSDK的文件中，增加头文件引用。并遵守PaymentDelegate协议
```xml
1.#import <PayssionSDK/PayssionSDK.h>

2.@interface ViewController () <PaymentDelegate>
3.@end
```

##### 3. 组装请求信息。
```xml
    PayRequest *payRequest = [[PayRequest alloc] init];
    payRequest.mLiveMode = false;
    payRequest.mAPIKey = @"916937a82dd7af5a";
    payRequest.mSecretKey = @"demo456";
    payRequest.mPMId = @"boleto_br";
    payRequest.mAmount = @"100";
    payRequest.mCurrency = @"USD";
    payRequest.mOrderId = @"order id 123";
    payRequest.mDescription = @"IOS order by payssion, orderId:123";
```
## 代码示例
###创建交易
通过以下代码创建交易
```java
    PaymentMainController *paymentMainController = [[PaymentMainController alloc] init];
    paymentMainController.payRequest = payRequest;
    paymentMainController.paymentDelegate = self;
    paymentMainController.hidesBottomBarWhenPushed = YES;
    [self.navigationController pushViewController:paymentMainController animated:YES];
```
###处理返回数据
需要重写`processOrderWithPayResponse`函数
```java
    - (void)processOrderWithPayResponse:(PayResponse *)payResponse{
    if (payResponse != nil) {
        NSLog(@"%@",payResponse.mOrderId);
    }
}
```

###常见疑问
1. 提示`pp Transport Security has blocked a cleartext HTTP (http://) resource load since it is insecure.`

上述问题需要`修改 App Transport Security 策略`

iOS 9 中新增了 App Transport Security 特性, 主要影响是使得原来请求网络数据时用到的 HTTP 协议都必须换成 HTTPS 协议。

但是实际情况中，我们的数据服务可能还并未升级到 HTTPS，所以这时候我们需要对项目进行一些设置来允许 HTTP 数据请求，也就是修改 App Transport Security 策略。步骤是：

在项目的 `Info.plist` 文件中添加如下字段：

![alt text](/libs/security.png "lib")

对应的字段如下：

```xml
<key>NSAppTransportSecurity</key>
<dict>
    <key>NSAllowsArbitraryLoads</key>
    <true/>
</dict>
```
