# flexy-AWSIotShadow-connector
a connector to make digital twins with Ewon Flexy and AwsIot

## How to use

1. Create a thing in AWS Iot and get the certificate and private key
2. Modify the policy of thing with the folowing one:
```
{
    "Version": "2012-10-17",
    "Statement": [{
            "Effect": "Allow",
            "Action": [
                "iot:Connect"
            ],
            "Resource": [
                "arn:aws:iot:<AwsRegion>:<AwsAccount>:client/${iot:Connection.Thing.ThingName}"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "iot:Publish",
                "iot:Receive"
            ],
            "Resource": [
                "arn:aws:iot:<AwsRegion>:<AwsAccount>:topic/$aws/things/${iot:Connection.Thing.ThingName}/shadow/*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "iot:Subscribe"
            ],
            "Resource": [
                "arn:aws:iot:<AwsRegion>:<AwsAccount>:topicfilter/$aws/things/${iot:Connection.Thing.ThingName}/shadow/*"
            ]
        }
    ]
}
```
4. Put the certficate and private key file in the usr directory of the ewon
5. Prepare a config.txt file to bootstrap the IoServer, after you can change it in the Ewon's website, the parameters are :
   
| parameter           | type    | description                                                                                       |
| -----------------   | ------- | ------------------------------------------------                                                  |
| `thingName`         | String  | The name of the thing you create in AWSIot                                                        |
| `caFile`            | String  | the ca file's path, download here   https://www.amazontrust.com/repository/AmazonRootCA1.pem      |
| `thingPublicCert`   | String  | the thing's public certificate file path                                                          |
| `thingPrivateKey`   | String  | the thing's private key certificate file path                                                     |
| `brokerAddress`     | String  | the iot broker adresse, you can find it in the Settings page of your AWS IoT Core console.        |
| `brokerPort`        | INT     | 8883 by default                                                                                   |
| `debug`             | Boolean | If true show some debug log                                                                       |
| `periodicFlushTime` | INT     | Time in second between full tag update on shadow                                                  |
| `periodicGroups`    | String  | Tag group for periodic sending (ABCD by defaut), if a tag has no group it is not updated on iot   |
| `onChangeGroups`    | String  | Tag group for onChange sending (ABCD by defaut), if a tag has no group it is not updated on iot   |


6. Write a jvmrun file with the folowing content
```
#IOSERVER
-heapsize 5M -classpath /usr/EwonAwsIot.jar -emain net.socometra.Main
```
7. Put the Jar file in /usr/
8. You can reboot the ewon to start the IOserver or use the url http://IpOfEwon/rcgi.bin/jvmCmd?cmd=start&runCmd=%20-heapsize%205M%20-classpath%20/usr/HelloWorld.jar%20-emain%20net.socometra.awsiot.Main 

## Usage of the shadow
Create a tag in the ewon in group A when the periodic time is done the shadow is updated like that :
```
{
  "state": {
    "reported": {
      "StrPoint": "Test ",
      "IntPoint": 1234,
      "BoolPoint": true
    }
  }
}
```

if a tag as an alarm, the shadow look like :
```
{
  "state": {
    "reported": {
      "intAlarmPoint": {
        "value": -21,
        "alStatus": 2,
        "alType": 2
      }
    }
  }
}
```
Signification code for Status and type (same as the ewon doc)
| `alStatus`  | Description |
| ----------  | ----------- |
| `0`         | None        |
| `2`         | Alarm       |
| `3`         | Ack         |
| `4`         | Return      |

| `alType`    | Description |
| ----------  | ----------- |
| `0`         | None        |
| `1`         | High        |
| `2`         | Low         |
| `3`         | Level       |
| `4`         | High High   |
| `5`         | Low Low     |

## Use the shadow to send data to the Ewon
If you want to update a Tag with the name `StrPoint` you just need to put a json in the update topic of your thing :
```
{
  "state": {
    "desired": {
      "StrPoint": "Hello World"
    }
  }
}
```
If the tag don't exist in ewon you will see error message in log :

`[AWIOT ERR] Error update Tag Value : StrPoint Err : java.lang.IllegalAccessException: Item not found`

## Update Tag Database
If you change the configuration of a Tag, the IoServer can't detect it. So you need to go in IoServer configuration and click on the Update buton to refresh the tag database.
