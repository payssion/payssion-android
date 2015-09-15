## payssion-android

## Installation
### Eclipse
1. Copy `PayssionSDK.jar` to the `libs/` folder.
2. Make sure you have added the following permission to the `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

## Usage
### Create a transaction
Lets say you create a transaction in `MainActivity`.
```java
Intent intent = new Intent(MainActivity.this, PayssionActivity.class);
intent.putExtra(PayssionActivity.ACTION_REQUEST, 
        new PayRequest()
        .setAPIKey(apiKey) //Your API Key
        .setAmount(amount)
        .setCurrency(currency)
        .setPMId(pmId)
        .setPayerRef(ref)
        .setDescription(description)
        .setTrackId(orderId) 
        .setSecretKey(secreKey)
        .setPayerEmail(payerEmail)
        .setPayerName(payerName));
MainActivity.this.startActivityForResult(intent, 0);
```

### Handle result
You need to overide `onActivityResult` method in `MainActivity`.
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
					String orderId = response.getTrackId(); //get your order id
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


