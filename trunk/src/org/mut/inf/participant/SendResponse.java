
package org.mut.inf.participant;

import java.io.Serializable;
import org.mut.inf.isohelper.ContextHelper;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;
import org.jpos.util.Log;
import org.mut.inf.isohelper.DataElementHelper;
import org.mut.inf.isohelper.ResponseCodeHelper;

/**
 * Transaction Participant for sending response message
 * to client wheter aborted transaction or success transaction
 *
 * @author Erwin
 * last update : 21 October 2010
 */
public class SendResponse implements AbortParticipant {

    private Log log;

    public SendResponse() {
        log = Log.getLog("Q2", getClass().getName());
    }

    
    public int prepare(long l, Serializable srlzbl) {
        Context ctx = (Context)srlzbl;
        ISOSource source = (ISOSource)ctx.get(ContextHelper.CONTEXT_SRC);
        if (source == null || !source.isConnected())
        {
             ctx.put(ContextHelper.CONTEXT_RESPONE_CODE, ResponseCodeHelper.UNCONNECTED_CHANNEL1);
            return ABORTED;
        }
            return PREPARED;
    }

   
    public int prepareForAbort(long l, Serializable srlzbl) {
        return prepare(l, srlzbl);
    }

    
    public void commit(long l, Serializable srlzbl) {
        sendResponse(l, (Context)srlzbl);
    }

   
    public void abort(long l, Serializable srlzbl) {
        
        sendResponse(l, (Context)srlzbl);
    }

    private void sendResponse (long l, Context ctx) {
        ISOSource source = (ISOSource) ctx.get (ContextHelper.CONTEXT_SRC);
        ISOMsg resp = (ISOMsg) ctx.get (ContextHelper.CONTEXT_RSP);
        String rc = (String) ctx.get (ContextHelper.CONTEXT_RESPONE_CODE);
         
      
        if (source == null || !source.isConnected()) {
            return;
        }
        try {
            if (source != null && source.isConnected() && resp != null)
                if(rc == null) {
                    rc = ResponseCodeHelper.UNKNOWN_ERROR;
                }
                if(resp.isRequest()) {
                    resp.setResponseMTI();
                }
//             resp.setHeader(ISOUtil.hex2byte(NetworkManagementHelper.Header_BAD));
                resp.set(DataElementHelper.RESPONSE_CODE, rc);
                
               
              
                source.send (resp);
                
                ctx.put(ContextHelper.CONTEXT_RSP, resp);
                ctx.put(ContextHelper.CONTEXT_RESPONE_CODE, rc);
                
        } catch (Throwable t) {
            log.error(this, t);
        }
    }

}
