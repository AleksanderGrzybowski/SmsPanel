package smspanel

import grails.validation.Validateable

class ScheduleMessagesDto implements Validateable {
    List<ScheduleSingleMessageDto> messages

    static constraints = {
        messages validator: { List<ScheduleSingleMessageDto> dtos, obj, errors ->
            if (!dtos) { // just returning doesn't work for some reason
                errors.rejectValue('messages', 'noMessages')
            }
            
            dtos.eachWithIndex { ScheduleSingleMessageDto dto, int index ->
                dto.validate()
                dto.errors.fieldErrors.each { error ->
                    errors.rejectValue("messages[$index].$error.field", error.code, error.arguments, error.defaultMessage)
                }
            }
        }
    }
}

    

