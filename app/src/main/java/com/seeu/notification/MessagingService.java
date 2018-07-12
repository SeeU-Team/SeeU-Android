package com.seeu.notification;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.seeu.TabbedActivity;
import com.seeu.chat.ChatActivity;
import com.seeu.chat.ConversationType;
import com.seeu.chat.MemberMessage;
import com.seeu.chat.Message;
import com.seeu.chat.TeamMessage;
import com.seeu.member.Member;
import com.seeu.member.MemberService;
import com.seeu.utils.SharedPreferencesManager;
import com.seeu.utils.network.CustomResponseListener;

import java.util.Map;

import static com.seeu.chat.ConversationType.TEAM_TO_BEFORE;

/**
 * Created by thomasfouan on 10/07/2018.
 */
public class MessagingService extends FirebaseMessagingService implements CustomResponseListener<Void> {

	private final Gson gson = new Gson();
	private NotificationSenderService notificationSenderService;
	private MemberService memberService;

	@Override
	public void onCreate() {
		super.onCreate();
		notificationSenderService = new NotificationSenderService();
		memberService = new MemberService(this);
	}

	@Override
	public void onNewToken(String token) {
		super.onNewToken(token);

		// Get updated InstanceID token.
		Log.d(this.getClass().getName(), "Refreshed token: " + token);

		Member currentUser = SharedPreferencesManager.getEntity(this, Member.STORAGE_KEY, Member.class);
		// At first launch, the user has not ever been connected, so it is null
		if (null != currentUser) {
			memberService.updateAppInstanceId(currentUser, token, this);
		}
	}

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		String typeStr = remoteMessage.getData().get("type");
		if (null == typeStr) {
			Log.d(this.getClass().getName(), "Notification Type not set...");
			return;
		}

		NotificationType type = NotificationType.valueOf(typeStr.toUpperCase());

		switch (type) {
			case MESSAGE:
				manageChatMessage(remoteMessage);
				break;
			case LIKE:
				manageLikeMessage(remoteMessage);
				break;
			case MERGE:
				manageMergeMessage(remoteMessage);
		}
	}

	private void manageChatMessage(RemoteMessage remoteMessage) {
		ConversationType conversationType = getConversationType(remoteMessage);
		if (null == conversationType) {
			return;
		}

		Message message = gson.fromJson(
				remoteMessage.getData().get("message"),
				TEAM_TO_BEFORE.equals(conversationType) ? TeamMessage.class : MemberMessage.class);

		if (TabbedActivity.isAppRunning
				&& ChatActivity.isActive) {
			Intent notificationIntent = notificationSenderService.getChatActivityIntent(this, message, conversationType);
			notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

			// Do not send notification if it is the current conversation, just send the intent
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
					PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
			try {
				pendingIntent.send();
			} catch (PendingIntent.CanceledException e) {
				e.printStackTrace();
			}
		} else {
			notificationSenderService.sendNewMessageNotificationWithStack(this, message, conversationType);
		}

		/*
		Intent notificationIntent = notificationSenderService.getChatActivityIntent(this, message, conversationType);
		PendingIntent pendingIntent;

		if (TabbedActivity.isAppRunning) {
			pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
					PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

			if (ChatActivity.isActive) {
				// Do not send notification if it is the current conversation, just send the intent
				try {
					pendingIntent.send();
				} catch (PendingIntent.CanceledException e) {
					e.printStackTrace();
				}
				return;
			}
		} else {
			// Start the pending intent with the stack
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
			stackBuilder.addNextIntentWithParentStack(notificationIntent);

			pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
		}

		notificationSenderService.sendNewMessageNotification(this, pendingIntent, message);
		*/
	}

	private void manageLikeMessage(RemoteMessage remoteMessage) {

	}

	private void manageMergeMessage(RemoteMessage remoteMessage) {

	}

	private ConversationType getConversationType(RemoteMessage remoteMessage) {
		String conversationTypeStr = remoteMessage.getData().get("conversationType");
		if (null == conversationTypeStr) {
			Log.d(this.getClass().getName(), "Conversation Type not set...");
			return null;
		}

		return ConversationType.valueOf(conversationTypeStr.toUpperCase());
	}

	@Override
	public void onHeadersResponse(Map<String, String> headers) {
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		error.printStackTrace();
	}

	@Override
	public void onResponse(Void response) {
		Log.i(this.getClass().getName(), "App instance ID updated successfully");
	}
}
