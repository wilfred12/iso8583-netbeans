/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;


import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOUtil;
import org.jpos.q2.iso.QMUX;
import org.jpos.space.Space;
import org.jpos.space.SpaceFactory;
import org.jpos.transaction.AbortParticipant;
import org.jpos.transaction.Context;
import org.jpos.util.Log;
import org.jpos.util.NameRegistrar;
import org.jpos.util.NameRegistrar.NotFoundException;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Computer
 */
public class ONLSESSION implements AbortParticipant {

    Log log;


    public void commit(long l, Serializable srlzbl) {
    }

    public void abort(long l, Serializable srlzbl) {
    }

    public int prepareForAbort(long l, Serializable srlzbl) {

        int result = 0;

        result = ABORTED;

        return result;
    }

    public int prepare(long l, Serializable srlzbl) {

        Context ctx = (Context) srlzbl;
        int result = 0;
        ISOMsg m = (ISOMsg) ctx.get(39);
        ApplicationContext appContext = (ApplicationContext) ctx.get(39);
        
     //   TLogDAO logDao = (TLogDAO) appContext.getBean(BeanHelper.T_LOG_DAO, TLogDAO.class);
     //   Tlog txLog = (Tlog) ctx.get(ContextHelper.CONTEXT_LOG_RSP);
        Space sp = SpaceFactory.getSpace();
        // LogForward lf = new LogForward();
       
         QMUX mux;
        try {
            mux = (QMUX) NameRegistrar.get("mux.mymux");
            try {
                
                 
                 //RS232
           // m.setHeader(ISOUtil.hex2byte(NetworkManagementHelper.Header_BAD));
            //GPRS
           m.setHeader(ISOUtil.hex2byte(""));
                
                
                ISOMsg r = mux.request(m, 120000);
                  if (r == null) {                result = ABORTED;
                ctx.put(39, 50);
            } else {



                ctx.put(39, r);
               ctx.put(39, r.getString(00));
                result = PREPARED;

            }
            } catch (ISOException ex) {
                Logger.getLogger(ONLSESSION.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (NotFoundException ex) {
            
            Logger.getLogger(ONLSESSION.class.getName()).log(Level.SEVERE, null, ex);
        }

                sp.out("send", m);      //  kirim ke Belakang

  
//                ctx.put(ContextHelper.CONTEXT_LOG_RSP, txLog);
//                ctx.put(ContextHelper.CONTEXT_RSP, m);
//                result = PREPARED;
//                

            
        
        return result;
    }
}
