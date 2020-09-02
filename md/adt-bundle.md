# PayssionSDK
## 安装

**我们为不便向Android Studio迁移的开发者提供该[版本支持](/libs/adt-bundle)。**

##### 1. 将文件`PayssionSDK.jar`拷贝到`libs/`文件夹
##### 2. 将`PayssionSDK.jar`Add to Build Path.    

##### 3. 在`AndroidManifest.xml`文件中添加以下权限:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
**以及以下Activity声明**
```xml
<activity
    android:name="com.payssion.android.sdk.PayssionActivity"
    android:label="@string/app_name"/>
<activity
    android:name="com.payssion.android.sdk.PaymentWebActivity"
    android:label="@string/app_name"/>
```

## 请求参数说明

| 方法名  	      | 参数名         | 类型         | 必填          | 示例          | 释义          | 详解         |
| :------------ | :------------ |:------------ |:--- |:--------------- |:---------------- |:--------------------- |
| setLiveMode   | live_mode     | boolean      | 否           | true/false    | App所在环境 |测试环境传false；正式环境传true。默认正式环境 |
| setAPIKey     | api_key       | string       | 是           | 5963a4c1c35c2a8e  | App id    | 注意区分对应环境的api_key |
| setTrackId    | track_id      | string       | 是           | 123       | 订单号     | 长度不超过32位
| setAmount     | amount        | double       | 是           | 1.99              | 订单总金额  | 
| setCurrency   | currency      | string       | 是           | USD        | 货币种类    | 大写，币种缩写可搜索公共信息
| setPMId       | pm_id          | string       | 是          | sofort             | 支付方式id | [供参考](https://payssion.com/en/docs/#api-reference-pm-id)。具体询问我司商务经理 |
| setDescription| description   | string       | 是           | game recharge #123 | 订单描述 | |
| setPayerEmail | payer_email   | string       | 是           | example@demo.com   | 付款方邮箱 | 南美地区支付必须填写邮箱。具体询问我司商务经理 |
| setPayerName  | payer_name    | string       | 是           | John Smith       | 付款方姓名 || 

## 使用
###创建交易
在`MainActivity`通过以下代码创建交易
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
        new PayRequest()
        .setLiveMode(false) //测试环境传false.正式环境传true.默认正式环境
        .setAPIKey(apiKey) //请注意区分测试环境和正式环境的APIKey
        .setAmount(amount)
        .setCurrency(currency)
        .setPMId(pmId)
        .setDescription(description)
        .setTrackId(trackId) //您的订单Id
        .setPayerEmail(payerEmail)
        .setPayerName(payerName));
MainActivity.this.startActivityForResult(intent, 0);
```
###处理返回数据
1. 需要在`MainActivity`重写`onActivityResult`函数
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(this.getClass().getSimpleName(), "onActivityResult");
        switch (resultCode) {
        case PayssionActivity.RESULT_PENDING:
            if (null != data) {
                PayResponse response = (PayResponse)data.getSerializableExtra(PayssionActivity.RESULT_DATA);
                if (null != response) {
                    String transId = response.getTransactionId(); //获取Payssion交易Id
					     String trackId = response.getTrackId(); //获取您的订单Id
                    //you will have to query the payment state with the transId or orderId from your server
                    //as we will notify you server whenever there is a payment state change
                } else {
                    //should never go here
                }
            }
            break;
        case PayssionActivity.RESULT_CANCELED:
            //the transation has been cancelled, for example, the users doesn't pay but get back
            break;
        case PayssionActivity.RESULT_ERROR:
            //there is some error
            if (null != data) {
                String err_des = data.getStringExtra(PayssionActivity.RESULT_DESCRIPTION);
                Log.v(this.getClass().getSimpleName(), "RESULT_ERROR" + err_des);   
            }
            break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
```
## 返回数据说明

###1.参数说明

返回数据封装为PayResponse,通过Intent.getSerializableExtra(PayssionActivity.RESULT_DATA)方法获取

| 方法名  	      | 参数名         | 类型          | 释义              | 详解                   |
| :------------ | :------------ |:------------ |:----------- |:------------------------- |
| getTransactionId   | transaction_id     | String      | 交易id|Payssion 交易号 id,如订单异常,需提供此 id
| getTrackId   | track_id     | String      | 订单id| 客户发起支付时自定义的订单 id。注意与`transaction_id`区分
| getState| state     | String      | 支付状态| 支付完成状态，准确支付结果以notify_url通知为准
| getAmount| amount     | String      | 订单金额|

###2.ResultCode说明

| 参数名  	        | 释义       | 类型 | 值
| :-------------- | :--------- | :--------- | :--------- |
| RESULT_PENDING       | 正常返回，但不代表支付成功，具体支付状态以异步通知为准    | int| 770
| RESULT_CANCELED | 支付取消    | int| 771
| RESULT_ERROR    | 支付异常    | int| 772


###3.注意事项
1. 需要注意的是这里的支付成功意味着支付流程成功，因为银行间结算有延迟，所以最终的支付结果要以您后台配置的`[notify_url](https://payssion.com/en/docs/#api-reference-payment-notifications)`收到的通知为准。

2. 本版本SDK在发送`notify_url`数据时的参数`notify_sig`与[文档](https://payssion.com/en/docs/#api-reference-signature)有区别。
需将加密规则`api_key|pm_id|amount|currency|order_id|state|sercret_key`改为`api_key|pm_id|amount|currency|track_id||state|sercret_key`。请将这一点告知负责接收`notify_url`的负责人。

3. 接收`notify_url`的[Demo](https://github.com/payssion/payssion-php/blob/master/samples/sample_postback.php)。
注意将接收参数`order_id`改为`track_id`。

![alt text](/img/_track_id.png "track_id")


##PMID设置
##### 1. 设置多个pmid
该版本PayRequest().setPMId(pmid)方法每次只能传入一个pmid，如需传入多个。建议将pmid数据封装并通过ListView展示，待用户点击某个item后将pmid传入PayRequest，后发起支付。或自行按业务需求设计逻辑。
我们为封装支付方式提供所需[部分图片](https://pan.baidu.com/s/1mhMppjE)、[部分pmid和名称](https://payssion.com/en/docs/#api-reference-pm-id)，完整数据请咨询我司商务经理。
