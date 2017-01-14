package io.customerly;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Spanned;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gianni on 11/09/16.
 * Project: CustomerlySDK
 */
class IE_Conversation {

    private static final int /*WRITER_TYPE__USER = 1,*/ WRITER_TYPE__ACCOUNT = 0;

    boolean unread;
    private Customerly.HtmlMessage last_message_abstract;
    private long last_message_writer, last_message_writer_type;
    long last_message_date;
    @Nullable private String last_account__name;

    final long conversation_id, assigner_id;

    IE_Conversation(long pConversationID, @Nullable Customerly.HtmlMessage pLastMessage, long pAssignerID, long pLastMessageDate, long pLastMessageWriterID, int pLastMessageWriterType, @Nullable String pLastAccountName) {
        super();
        this.conversation_id = pConversationID;
        this.last_message_abstract = pLastMessage;
        this.assigner_id = pAssignerID;

        this.last_message_date = pLastMessageDate;
        this.last_message_writer = pLastMessageWriterID;
        this.last_message_writer_type = pLastMessageWriterType;

        this.unread = true;

        this.last_account__name = pLastAccountName;
    }

    IE_Conversation(@NonNull JSONObject pConversationItem) throws JSONException {
        super();

        this.conversation_id = pConversationItem.getLong("conversation_id");
        this.last_message_abstract = IU_Utils.decodeHtmlStringWithEmojiTag(pConversationItem.getString("last_message_abstract"));
        this.assigner_id = pConversationItem.getLong("assigner_id");

        this.last_message_date = pConversationItem.getLong("last_message_date");
        this.last_message_writer = pConversationItem.optLong("last_message_writer");
        this.last_message_writer_type = pConversationItem.optInt("last_message_writer_type");

        this.unread = pConversationItem.optInt("unread", 0) == 1;

        pConversationItem = pConversationItem.optJSONObject("last_account");
        this.last_account__name = pConversationItem == null ? null : IU_Utils.jsonOptStringWithNullCheck(pConversationItem, "name");
    }

    void onNewMessage(@NonNull IE_Message pNewMessage) {
        this.last_message_abstract = pNewMessage.content;
        this.last_message_date = pNewMessage.sent_date;
        this.last_message_writer = pNewMessage.getWriterID();
        this.last_message_writer_type = pNewMessage.getWriterType();
        this.unread = true;
        this.last_account__name = pNewMessage.if_account__name;
    }

    @NonNull String getImageUrl(int pPixelSize) {
        return this.last_message_writer_type == WRITER_TYPE__ACCOUNT
            ? IE_Account.getAccountImageUrl(this.last_message_writer, pPixelSize)
            : IE_Account.getUserImageUrl(this.last_message_writer, pPixelSize);
    }

    @NonNull String getConversationLastWriter(@NonNull Context pContext) {
        return this.last_account__name != null ? this.last_account__name : pContext.getString(R.string.io_customerly__you);
    }

    @Nullable Spanned getLastMessage() {
        return this.last_message_abstract;
    }

    @NonNull String getFormattedLastMessageTime(@NonNull Resources resources) {
        return IU_TimeAgoUtils.calculate(this.last_message_date,
                seconds -> resources.getString(R.string.io_customerly__XXs_ago, seconds),
                minutes -> resources.getString(R.string.io_customerly__XXm_ago, minutes),
                hours -> resources.getString(R.string.io_customerly__XXh_ago, hours),
                days -> resources.getString(R.string.io_customerly__XXd_ago, days));
    }

}