package org.bad.inf.txparticipant;

import java.io.Serializable;
import org.mut.inf.isohelper.ContextHelper;
import org.jpos.core.Configurable;
import org.jpos.core.Configuration;
import org.jpos.core.ConfigurationException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.jpos.transaction.Context;
import org.jpos.transaction.GroupSelector;
import org.jpos.util.Log;
import org.mut.inf.isohelper.DataElementHelper;
import org.mut.inf.isohelper.MessageTypeHelper;
import org.mut.inf.isohelper.ResponseCodeHelper;

public class Switch implements GroupSelector, Configurable {

    private Configuration cfg;
    private Log log;

    public Switch() {
        log = Log.getLog("Q2", getClass().getName());
    }

    
    public String select(long l, Serializable srlzbl) {
        Context ctx = (Context) srlzbl;
        ISOMsg m = (ISOMsg) ctx.get(ContextHelper.CONTEXT_REQ);

        String groups = "";
        try {
            if (m.getMTI().equals(MessageTypeHelper.RECONSILIATION) || m.hasField(DataElementHelper.NETWORK_MANAGEMENT_INFO)||m.getMTI().equals(MessageTypeHelper.PAYMENT)) {
                groups = cfg.get(m.getMTI(), "default");

            } else {

                    groups = cfg.get(m.getMTI() + "." + m.getString(3), "default");
                
            }


            if (groups.equals("default")) {    // klw ga dapat apa apa
                if (m.isRequest()) {
                    m.setResponseMTI();             // balasan MTI
                }
                m.set(DataElementHelper.RESPONSE_CODE, ResponseCodeHelper.UNKNOWN_ERROR);  // respone code set M9 ato unknown mesg
                ISOSource src = (ISOSource) ctx.get(ContextHelper.CONTEXT_SRC);
                src.send(m);    //balikin lagi sourcenya
            }
        } catch (Exception ex) {
            log.error(this, ex);      //
        }


        //log.error(this, groups);
        return groups;               //     kebalikan nilai groups isinya kelihatanya  0100.750000
        // yang ada di 20_txn_mgr.xml

    }

   
    public int prepare(long l, Serializable srlzbl) {
        return PREPARED;
    }

    
    public void commit(long l, Serializable srlzbl) {
    }

    
    public void abort(long l, Serializable srlzbl) {
    }

    
    public void setConfiguration(Configuration c) throws ConfigurationException {
        this.cfg = c;
    }
}
