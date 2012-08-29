/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bad.inf.txparticipant;


import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mut.inf.isohelper.BeanHelper;
import org.mut.inf.isohelper.ContextHelper;
import org.mut.inf.isohelper.DataElementHelper;
import org.mut.inf.isohelper.ResponseCodeHelper;

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
public class ONLTrans implements AbortParticipant {

    Log log;
  

    
    public void commit(long l, Serializable srlzbl) {
    }

    
    public void abort(long l, Serializable srlzbl) {

    }

    
    public int prepareForAbort(long l, Serializable srlzbl) {

              Context ctx = (Context) srlzbl;
        int result = 0;
        ISOMsg m = (ISOMsg) ctx.get(ContextHelper.CONTEXT_RSP);
        ApplicationContext appContext = (ApplicationContext) ctx.get(ContextHelper.CONTEXT_SPRING);

        Space sp = SpaceFactory.getSpace();

        result = ABORTED;

        return result;
    }

  
    public int prepare(long l, Serializable srlzbl) {

        Context ctx = (Context) srlzbl;
        int result = 0;
        ISOMsg m = (ISOMsg) ctx.get(ContextHelper.CONTEXT_RSP);
        ApplicationContext appContext = (ApplicationContext) ctx.get(ContextHelper.CONTEXT_SPRING);
        Space sp = SpaceFactory.getSpace();
      



        QMUX mux;
        try {
            mux = (QMUX) NameRegistrar.get("mux.mymux");


         //   m.setHeader(ISOUtil.hex2byte(""));
            ISOMsg r = mux.request(m, 120000);


            if (r == null) {              
                result = ABORTED;
                ctx.put(ContextHelper.CONTEXT_RESPONE_CODE, ResponseCodeHelper.TIMEOUT);
           
            } else {



                ctx.put(ContextHelper.CONTEXT_RSP, r);
               ctx.put(ContextHelper.CONTEXT_RESPONE_CODE, r.getString(DataElementHelper.RESPONSE_CODE));
                result = PREPARED;

            }




        } catch (NotFoundException ex) {
            Logger.getLogger(ONLTrans.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ISOException ex) {
            Logger.getLogger(ONLTrans.class.getName()).log(Level.SEVERE, null, ex);
        }




        return result;
    }
}
