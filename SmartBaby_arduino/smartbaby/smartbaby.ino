//시리얼통신 라이브러리 호출

#include <SoftwareSerial.h>
int blueTx=2;
int blueRx=3;

int button = 8;
int soundPin = A0;
int piezo = 7;


//시리얼 통신을 위한 객체선언
SoftwareSerial mySerial(blueTx, blueRx);

void setup(){
  //시리얼모니터
  Serial.begin(9600);
  //블루투스 시리얼
  mySerial.begin(9600);

  pinMode(button, INPUT);
  pinMode(soundPin, INPUT);
}

void loop() {

// 안드로이드로부터 받을 신호
  if (mySerial.available()) {       

    char toSend = (char)mySerial.read();
    Serial.print(toSend);
    //안드로이드에서 1을 전송받으면 piezo 작동
    if(toSend=='1') {
        tone(piezo, 262, 2000);
        delay(400);
        noTone(piezo);
    }
  }
  
  


 // 버튼이 눌리면 안드로이드로 "sleeping" 전송
  if(digitalRead(button) == HIGH) {
    mySerial.println("sleeping");
    
  }

// 소리감지센서로 1000이상의 값을 읽으면 안드로이드로 "wake"전송
  if(analogRead(soundPin) >= 1000) {
    mySerial.println("wake");
    
    
  }
  

  delay(100);
  

}
