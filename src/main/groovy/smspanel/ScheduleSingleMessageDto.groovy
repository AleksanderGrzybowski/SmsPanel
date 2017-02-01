package smspanel

import grails.validation.Validateable

class ScheduleSingleMessageDto implements Validateable {
    Long contactId
    String content

    static constraints = {
        contactId validator: { Long val ->
            Contact.exists(val) ?: 'nonexistentContact'
        }
        
        content nullable: false, blank: false, size: 0..160
    }
}

