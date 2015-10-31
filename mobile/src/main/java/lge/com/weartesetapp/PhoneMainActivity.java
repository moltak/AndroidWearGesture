package lge.com.weartesetapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PhoneMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_main);
    }

    public void sendMessage(View view) {
        // 첫번째 버튼 클릭
        int notiId = 1;
        String eventTitle = "Hello";
        String eventText = "Good night~!!";
        // Build intent for notification content
        Intent viewIntent = new Intent(this, PhoneMainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle(eventTitle)
                        .setContentText(eventText)
                        .setContentIntent(viewPendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(notiId, notificationBuilder.build());
    }

    public void sendMessage2(View view) {
        // 두번째 버튼 클릭
        int notiId = 2;
        Intent viewIntent = new Intent(this, PhoneMainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        Uri geoUri = Uri.parse("geo:37.4820434,126.8795371" );
        mapIntent.setData(geoUri);
        PendingIntent mapPendingIntent =
                PendingIntent.getActivity(this, 0, mapIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert)
                        .setContentTitle("제목")
                        .setContentText("버튼도 추가요~!")
                        .setContentIntent(viewPendingIntent)
                        .addAction(android.R.drawable.ic_dialog_map,
                                "추가기능", mapPendingIntent);

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        notificationManager.notify(notiId, notificationBuilder.build());
    }

    public void sendMessage3(View view) {
        // 세번째 버튼 클릭
        int notiId = 3;
        Intent viewIntent = new Intent(this, PhoneMainActivity.class);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        // 첫번째 페이지 노티
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentTitle("1 page")
                        .setContentText("하이~!")
                        .setContentIntent(viewPendingIntent);

        // 두번째 페이지 스타일
                NotificationCompat.BigTextStyle secondPageStyle = new NotificationCompat.BigTextStyle();
                secondPageStyle.setBigContentTitle("2 page")
                        .bigText("안녕하세요. 엘지전자 입니다. V10 많이 사랑해주세요~ +_+ ");

        // 두번째 페이지
                Notification secondPageNotification =
                        new NotificationCompat.Builder(this)
                                .setStyle(secondPageStyle)
                                .build();

        // 두번째 페이지 확장
                Notification notification = notificationBuilder
                        .extend(new NotificationCompat.WearableExtender()
                                .addPage(secondPageNotification))
                        .build();

        NotificationManagerCompat notificationManager =
                        NotificationManagerCompat.from(this);
                notificationManager.notify(notiId, notification);
    }
}
