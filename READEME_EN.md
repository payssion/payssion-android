
# PayssionSDK
## Installation
##### 1. 将文件`PayssionSDK.jar`拷贝到`libs/`文件夹

`Android Studio`需要如下配置：

配置app级别build.gradle,在dependencies中增加以下compile
 - compile 'com.android.support:appcompat-v7:xx.xx'
 - compile 'com.android.support:design:xx.xx'
 - compile files('libs/PayssionSDK.jar')

`Eclipse`需要如下配置：

 - 导入android-support-v7-appcompat库.
 - 导入android-support-design库.
 - 将`PayssionSDK.jar`Add to Build Path.    

##### 2. 在`AndroidManifest.xml`文件中添加以下权限:
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```
**以及以下Activity**
```xml
<activity
    android:name="com.payssion.android.sdk.PayssionActivity"
    android:label="@string/app_name"
    android:theme="@style/Theme.PayssionTrans"/>
<activity
    android:name="com.payssion.android.sdk.PaymentWebActivity"
    android:label="@string/app_name"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"/>
```

##### 3. 在values/styles.xml文件中添加以下style
```xml
   <style name="Theme.PayssionTrans" parent="Theme.AppCompat.Light">
        <item name="android:windowIsTranslucent">true</item>
        <item name="android:windowBackground">@android:color/transparent</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowIsFloating">true</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:backgroundDimEnabled">true</item>
    </style>
```
## Usage
###创建交易
在`MainActivity`通过以下代码创建交易
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
        new PayRequest()
        .setLiveMode(false) //false if you are using sandbox environment
        .setAPIKey(apiKey) //Your API Key
        .setAmount(amount)
        .setCurrency(currency)
        .setPMId(pmId)
        .setDescription(description)
        .setOrderId(orderId) //Your order id
        .setSecretKey(secreKey)
        .setPayerEmail(payerEmail)
        .setPayerName(payerName));
MainActivity.this.startActivityForResult(intent, 0);
```
###处理返回数据
需要在`MainActivity`重写`onActivityResult`函数
```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(this.getClass().getSimpleName(), "onActivityResult");
        switch (resultCode) {
        case PayssionActivity.RESULT_OK:
            if (null != data) {
                PayResponse response = (PayResponse)data.getSerializableExtra(PayssionActivity.RESULT_DATA);
                if (null != response) {
                    String transId = response.getTransactionId(); //get Payssion transaction id
					String orderId = response.getOrderId(); //get your order id
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
##PMID设置
##### 1. 您可以设置仅支持`一种支付方式`.  
在创建交易的`PayRequest`中设置  

**例:**
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST,
new PayRequest()
    .setLiveMode(false) //false if you are using sandbox environment
    .setAPIKey(apiKey) //Your API Key
    .setAmount(amount)
    .setCurrency(currency)
    .setPMId(pmId)
    ...
MainActivity.this.startActivityForResult(intent, 0);
```
##### 2. 您可以设置支持`多种支付方式`.  
我们提供以下两种函数支持您合理配置所需的支付方式.  
您可以使用我们提供的支付方式`logo`,也可以在您工程的`assets`文件夹创建`”payssion/pm/“`路径,并将您自定义的支付方式`logo图片`放到该路径下.  
图片格式默认为`.png`,图片名称为该支付方式的`PMID`.
```java
   /**
     * 设置需要支持的支付方式
     * @param enablePM 需支持的PMID.如需多个可用“|”分割开。比如"pm_idA|pm_idB|pm_idC"
     */
    PayssionConfig.enablePM(String enablePM);

   /**
     * 设置不需要支持的支付方式
     * @param disEnablePM 不需支持的PMID.如需多个可用“|”分割开。比如"pm_idA|pm_idB|pm_idC"
     */
    PayssionConfig.disenablePM(String disenablePM);
```
### 设置主题颜色
您可以通过以下函数设置Theme颜色.
```java
   /**
     * Set Theme Color
     * @param color theme color
     */
PayssionConfig.setThemeColor(@ColorInt int color);
```
默认颜色是通过读取
```java
getTheme().resolveAttribute(android.R.attr.colorAccent, TypedValue, true);  
defaultColor = TypedValue.data;
```
获得.

**\*注意*** PayssionConfig相关的函数需要在创建交易之前设置  

### 多语言设置
我们支持汉语简体(ZH_SIMPLIFIED)、汉语繁体(ZH_TRADITIONAL)、英语(EN)、德语(DE)、西班牙语(ES)、葡萄牙语(PT)、俄语(RU)、阿拉伯语(AR)等语种.  
您可以通过`PayRequest.setLanguage(String language)`函数或者`PayssionConfig.setLanguage(String language)`设置语言,传入参数为`**PLanguage**`类的语种变量.  

**例:**  
```java
PayRequest payRequest = new PayRequest();
payRequest.setLanguage(PLanguage.ZH_SIMPLIFIED);
...
```
或
```java
PayssionConfig.setLanguage(PLanguage.ZH_SIMPLIFIED);
...
```

默认语种会通过`java.util.Locale.getDefault().getLanguage()`函数读取本地语言设置.

**\*注意***  
1. PayRequest设置语言的优先级要大于PayssionConfig.  
2. 阿拉伯语需要在`AndroidManifest.xml`文件中的`application`元素中加入`android:supportsRtl="true"`属性.
    该属性支持Android4.2及以上版本.  
    
**例:**  
```xml
<application
    android:allowBackup="true"
    android:icon="@mipmap/ic_launcher"
    android:label="@string/app_name"
    android:supportsRtl="true"
    android:theme="@style/AppTheme">
    ...
</application>
```
    
