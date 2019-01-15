#define C  262
#define D  294
#define E  330
#define F  349
#define G  392
#define A  440
#define B  494
#define CC  523
#define DD  587
#define EE  659
#define FF  698
#define GG  784
#define AA  880
#define BB  988
#define CCC 1047
#define DDD 1175


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
        schoolbell();
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

void setSound(int piezo, int note, int meter) {
  tone(piezo, note, meter);
  delay(meter);
  noTone(piezo);
}

void schoolbell(){
  setSound(piezo, G, 300);
  setSound(piezo, G, 300);
  setSound(piezo, A, 300);
  setSound(piezo, A, 300);
  setSound(piezo, G, 300);
  setSound(piezo, G, 300);
  setSound(piezo, E, 600);
  setSound(piezo, G, 300);
  setSound(piezo, G, 300);
  setSound(piezo, E, 300);
  setSound(piezo, E, 300);
  setSound(piezo, D, 900);
  setSound(piezo, G, 300);
  setSound(piezo, G, 300);
  setSound(piezo, A, 300);
  setSound(piezo, A, 300);
  setSound(piezo, G, 300);
  setSound(piezo, G, 300);
  setSound(piezo, E, 600);
  setSound(piezo, G, 300);
  setSound(piezo, E, 300);
  setSound(piezo, D, 300);
  setSound(piezo, E, 300);
  setSound(piezo, C, 900);
  
}
