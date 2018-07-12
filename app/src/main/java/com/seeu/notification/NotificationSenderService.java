package com.seeu.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.seeu.R;
import com.seeu.chat.ChatActivity;
import com.seeu.chat.ConversationType;
import com.seeu.chat.Message;
import com.seeu.member.Member;
import com.seeu.member.MemberHasTeam;
import com.seeu.team.Team;
import com.seeu.utils.SharedPreferencesManager;

import java.util.Random;

/**
 * Created by thomasfouan on 11/07/2018.
 */
public class NotificationSenderService {
	private static final String MESSAGE_CHANNEL_ID = "messageChannelId";
	private static final String MESSAGE_CHANNEL_NAME = "SeeU Messages";
	private static final String MESSAGE_CHANNEL_DESC = "Messages reçues via SeeU";

	public Intent getChatActivityIntent(Context context, Message message, ConversationType type) {
		Intent intent;
		intent = new Intent(context, ChatActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(Message.STORAGE_KEY, message);

		switch (type) {
			case USER_TO_USER:
				intent.putExtra(Member.STORAGE_KEY, message.getOwner());
				break;
			case USER_TO_TEAM:
				MemberHasTeam memberHasTeam = SharedPreferencesManager.getObject(context, MemberHasTeam.STORAGE_KEY, MemberHasTeam.class);
				intent.putExtra(Team.STORAGE_KEY, memberHasTeam.getTeam());
				break;
			case TEAM_TO_BEFORE:
				intent.putExtra(Team.STORAGE_KEY, message.getOwner());
			default:
				break;
		}

		return intent;
	}

	public void sendNewMessageNotification(Context context, PendingIntent pendingIntent, Message message) {
		// Setting up notification channels for android O and above
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			setupChannels(context);
		}

		int notificationId = new Random().nextInt(60000);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		Notification notificationBuilder = new NotificationCompat.Builder(context, MESSAGE_CHANNEL_ID)
				.setSmallIcon(R.drawable.ic_notifications_black) // a resource for a custom small icon
				.setContentTitle(message.getOwner().getName())
				.setContentText(message.getContent())
				.setAutoCancel(true) // dismisses the notification on click
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent)
				.build();

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null) {
			notificationManager.notify(notificationId, notificationBuilder);
		}
	}

	public void sendNewMessageNotificationWithStack(Context context, Message message, ConversationType type) {
		Intent notificationIntent = getChatActivityIntent(context, message, type);

		sendNewMessageNotificationWithStack(context, message, notificationIntent);
	}

	public void sendNewMessageNotificationWithStack(Context context, Message message, Intent notificationIntent) {
		// Start the pending intent with the stack
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntentWithParentStack(notificationIntent);

		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

		sendNewMessageNotification(context, pendingIntent, message);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void setupChannels(Context context) {

		NotificationChannel messageChannel;
		messageChannel = new NotificationChannel(MESSAGE_CHANNEL_ID, MESSAGE_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
		messageChannel.setDescription(MESSAGE_CHANNEL_DESC);
		messageChannel.enableLights(true);
		messageChannel.setLightColor(Color.RED);
		messageChannel.enableVibration(true);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		if (notificationManager != null) {
			notificationManager.createNotificationChannel(messageChannel);
		}
	}
}
