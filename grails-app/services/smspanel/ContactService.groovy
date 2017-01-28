package smspanel

import grails.transaction.Transactional

@Transactional
class ContactService {

    List<Contact> list() {
        Contact.list()
    }
}
