# 常见问题答疑
## 简述
##### 1.该SDK使用需区分正式环境和测试环境。

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
##### 2.该SDK代码已混淆，无需重复混淆。

##### 3.SDK使用中有偶发的网络卡顿问题，请重试或使用代理。

## 参数调用   
##### Q1. 提示"api_key was not found"

**A1.** 请检查SDK当前运行环境是否与使用的APIKey相对应。

##### Q2. 提示"invalid api_sig"

**A2.** 请检查SDK当前运行环境是否与使用的SecretKey相对应。

##### Q3. 提示"invalid payment_method"

**A3.** 1 .请检查pmid填写是否正确。如pmid填写正确请参考第2步

        2 .因测试环境仅支持部分支付方式，请使用sofort、alipay_cn等支付方式测试。具体请咨询我方商务
    
##### Q4. 提示"this paymet option is not enabled for this merchant.Please check it and let the merchant know if needed"

**A4.** 该支付方式需申请开通，请联系我方商务开通。

##### Q5. 提示“The currency of `A` can only be `B`.You were trying to pay with `C`”

**A5.** 支付方式`A`只支持币种`B`创建的交易，你使用的币种是`C`。请将setCurrency(String)方法参数改为`B`。举例如下：

 ![alt text](/img/_currency.png "currency")

 解决该问题需要更改参数为setCurrency(“IDR”)。

##### Q6. 第三方支付提示创建交易异常，如：支付宝。举例如下：

![alt text](/img/_alipay.png "currency")

**A6.** 1 .该问题是因为支付宝交易有最低限额，美元交易最低为0.01USD。人民币交易最低为0.1CNY。请检查，如无异常请查看第2步

         2 .网络问题，请检查网络后重试。
    
##### Q7. 查询订单详情参数异常

**A7.** 如下例，在调用该方法传入参数时也需要注意传入的key以及liveMode是否一致。
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

**A8.** setOrderId(String)传入的订单号需要用户自定义。定义规则建议使用a-z+0-9,长度不超过32位。

##### Q9. 提示"No support for payment methods"

**A9.** 1.SDK判断当前手机的国家没有支持的支付(判断逻辑见`Q10`)，请检查是否使用PayssionConfig类对pmid显示做了过多的限制。如需要
        接入的支付方式不多，不建议使用PayssionConfig类限制，而建议自己构建支付列表，用Payssion.setPMId(pmid)传参的方式实现。  
        如设置后依然显示该提示，请参考第2条。
        
        2.请手动清除APP数据后重试。
##### Q10. SDK判断当前国家的逻辑

**A10.** SDK是根据当前所在国家展示相对应的支付列表，判断当前国家的方式是优先通过SIM卡判断国家，如无sim则判断手机设置和网络。

##### Q11. SDK界面是否支持横屏显示

**A11.** 暂不支持横屏

##### Q12. PayRequest中的setPayerEmail和setPayerName可以不填写吗

**A12.** 邮箱和用户名可以不填，SDK会根据需要要求用户填写，但为了用户体验以及方便您管理订单及用户，建议事先填写。

##### Q13. 在弹出的支付列表界面选择其他国家的支付方式并成功支付后支付列表就显示该国家的支付方式了

**A13.** 这是SDK的正常逻辑，以方便用户使用。

##### Q14. 在使用了`-libraryjars`,`-dontwarn`,`-keep class`等避免jar包混淆的措施后，依然报错。不能正常打包。

**A14.** [请点击下载](https://pan.baidu.com/s/1boLmaU7)，解压`proguard.zip`出的文件替换到`你本地sdk/tools/proguard/`，并重启IDE后重新打包











