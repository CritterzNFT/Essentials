package com.github.puregero.essentials.sync;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

import java.io.Serializable;
import java.util.UUID;

public class ReplyRecipientSynchronizer extends MultiServerSynchronizer<ReplyRecipientSynchronizer.Notification> {

    private final Essentials essentials;
    private boolean handling;

    public ReplyRecipientSynchronizer(Essentials essentials) {
        super(essentials, "essentials:reply_recipient");
        this.essentials = essentials;
        this.addHandler(this::handle);
    }

    private void handle(Notification notification) {
        User user = essentials.getUser(notification.getUuid());
        User replyRecipient = essentials.getUser(notification.getReplyRecipient());
        if (user != null && replyRecipient != null) {
            handling = true;
            user.setReplyRecipient(replyRecipient);
            handling = false;
        }
    }

    public void notify(User user, User replyRecipient) {
        if (!handling) {
            super.notify(new Notification(user, replyRecipient));
        }
    }

    public static class Notification implements Serializable {
        private final UUID uuid;
        private final UUID replyRecipient;

        public Notification(User user, User replyRecipient) {
            this.uuid = user.getUUID();
            this.replyRecipient = replyRecipient.getUUID();
        }

        public UUID getUuid() {
            return uuid;
        }

        public UUID getReplyRecipient() {
            return replyRecipient;
        }
    }

}
