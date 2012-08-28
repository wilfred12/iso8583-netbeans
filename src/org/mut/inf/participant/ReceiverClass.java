/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mut.inf.participant;

import java.io.Serializable;
import org.jpos.iso.ISOMsg;
import org.jpos.transaction.Context;
import org.jpos.transaction.TransactionParticipant;
import org.jpos.util.Log;
import org.mut.inf.isohelper.ContextHelper;
/**
 *
 * @author andybex andy@hartono.me
 */
public class ReceiverClass implements TransactionParticipant {
     
    private Log log;

    public ReceiverClass() {
        log = Log.getLog("Q2", getClass().getName());   

    }


   
    public int prepare(long l, Serializable srlzbl) {
        Context ctx = (Context) srlzbl;
        ISOMsg m = (ISOMsg) ctx.get(ContextHelper.CONTEXT_REQ);
        int result = 0;
        
        result = PREPARED;
        
       ctx.put(ContextHelper.CONTEXT_RSP, m);
        return result;     
    }

    
    public void commit(long l, Serializable srlzbl) {
    }

    
    public void abort(long l, Serializable srlzbl) {
    }

    
    
}
