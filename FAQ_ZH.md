# 常见问题答疑
## 简述
1.该SDK使用需区分正式环境和测试环境。

该SDK使用`PayRequest.setLiveMode(boolean)`方法区分运行环境，且APIKey和SecretKey(以下简称`key`)需是对应环境下申请的。如下例:
```java
new PayRequest()
        // 传true为正式环境，传false为测试环境。默认为正式环境
        .setLiveMode(false)
        // 传入对应环境的APIKey
        .setAPIKey(String)
        // 传入对应环境的SecretKey
        .setSecretKey(String)
        ...
```
2.该SDK代码已混淆，无需重复混淆。

## 参数调用   
##### Q1. 提示"api_key was not found"

A1.请检查SDK当前运行环境是否与使用的APIKey相对应。

##### Q2. 提示"invalid api_sig"

A2.请检查SDK当前运行环境是否与使用的SecretKey相对应。

##### Q3. 提示"invalid payment_method"

A3.1.请检查pmid填写是否正确。如pmid填写正确请参考第2步
   2.因测试环境仅支持部分支付方式，请使用sofort、alipay_cn等支付方式测试。具体请咨询我方商务
##### Q4. 提示"this paymet option is not enabled for this merchant.Please check it and let the merchant know if needed"

A4.该支付方式需申请开通，请联系我方商务开通。

##### Q5. 提示“The currency of `A` can only be `B`.You were trying to pay with `C`”

A5.支付方式`A`只支持币种`B`创建的交易，你使用的币种是`C`。请将setCurrency(String)方法参数改为`B`。举例如下：
![alt text](/img/_currency.png "currency")
解决该问题需要更改参数为setCurrency(“IDR”)。

##### Q6. 第三方支付提示创建交易异常，如：支付宝。举例如下：
![alt text](/img/_alipay.png "currency")

A6.1.该问题是因为支付宝交易有最低限额，美元交易最低为0.01USD。人民币交易最低为0.1CNY。请检查，如无异常请查看第2步
   2.网络问题，请检查网络后重试。
##### Q7. 查询订单详情参数异常

A7.如下例，在调用该方法传入参数时也需要注意传入的key以及liveMode是否一致。
```java
Payssion.getDetail(new GetDetailRequest()
        .setLiveMode(true)
        .setAPIKey("916937a82dd7af5a")
        .setSecretKey("demo456")
        .setTransactionId(transId)
        .setOrderId(orderId)
        ...
```

##### Q8. 定义orderId的问题

A8.setOrderId(String)传入的订单号需要用户自定义。定义规则建议使用a-z+0-9,长度不超过32位。
