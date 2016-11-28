# PayssionSDK
## 导入代码
##### 1. 将文件`PayssionSDK.framework`拷贝到项目文件夹下，并导入到项目工程中。   
在Build Phases选项卡的Link Binary With Libraries中，增加以下依赖：


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
