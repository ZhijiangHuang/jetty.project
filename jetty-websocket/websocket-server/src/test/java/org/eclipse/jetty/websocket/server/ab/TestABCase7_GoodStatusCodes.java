//
//  ========================================================================
//  Copyright (c) 1995-2013 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.server.ab;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.common.OpCode;
import org.eclipse.jetty.websocket.common.WebSocketFrame;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test Good Close Status Codes
 */
@RunWith(value = Parameterized.class)
public class TestABCase7_GoodStatusCodes extends AbstractABCase
{
    private static final Logger LOG = Log.getLogger(TestABCase7_GoodStatusCodes.class);

    @Parameters
    public static Collection<Object[]> data()
    {
        // The various Good UTF8 sequences as a String (hex form)
        List<Object[]> data = new ArrayList<>();

        // @formatter:off
        data.add(new Object[] { "7.7.1", 1000 });
        data.add(new Object[] { "7.7.2", 1001 });
        data.add(new Object[] { "7.7.3", 1002 });
        data.add(new Object[] { "7.7.4", 1003 });
        data.add(new Object[] { "7.7.5", 1007 });
        data.add(new Object[] { "7.7.6", 1008 });
        data.add(new Object[] { "7.7.7", 1009 });
        data.add(new Object[] { "7.7.8", 1010 });
        data.add(new Object[] { "7.7.9", 1011 });
        data.add(new Object[] { "7.7.10", 3000 });
        data.add(new Object[] { "7.7.11", 3999 });
        data.add(new Object[] { "7.7.12", 4000 });
        data.add(new Object[] { "7.7.13", 4999 });
        // @formatter:on

        return data;
    }

    private final int statusCode;

    public TestABCase7_GoodStatusCodes(String testId, int statusCode)
    {
        LOG.debug("Test ID: {}",testId);
        this.statusCode = statusCode;
    }

    /**
     * just the close code, no reason
     */
    @Test
    public void testStatusCode() throws Exception
    {
        ByteBuffer payload = ByteBuffer.allocateDirect(256);
        BufferUtil.clearToFill(payload);
        payload.putChar((char)statusCode);
        BufferUtil.flipToFlush(payload,0);

        List<WebSocketFrame> send = new ArrayList<>();
        send.add(new WebSocketFrame(OpCode.CLOSE).setPayload(payload.slice()));

        List<WebSocketFrame> expect = new ArrayList<>();
        expect.add(new WebSocketFrame(OpCode.CLOSE).setPayload(payload.slice()));

        Fuzzer fuzzer = new Fuzzer(this);
        try
        {
            fuzzer.connect();
            fuzzer.setSendMode(Fuzzer.SendMode.BULK);
            fuzzer.send(send);
            fuzzer.expect(expect);
            fuzzer.expectNoMoreFrames();
        }
        finally
        {
            fuzzer.close();
        }
    }

    /**
     * the good close code, with reason
     */
    @Test
    public void testStatusCodeWithReason() throws Exception
    {
        ByteBuffer payload = ByteBuffer.allocateDirect(256);
        BufferUtil.clearToFill(payload);
        payload.putChar((char)statusCode);
        payload.put(StringUtil.getBytes("Reason"));
        BufferUtil.flipToFlush(payload,0);

        List<WebSocketFrame> send = new ArrayList<>();
        send.add(new WebSocketFrame(OpCode.CLOSE).setPayload(payload.slice()));

        List<WebSocketFrame> expect = new ArrayList<>();
        expect.add(new WebSocketFrame(OpCode.CLOSE).setPayload(payload.slice()));

        Fuzzer fuzzer = new Fuzzer(this);
        try
        {
            fuzzer.connect();
            fuzzer.setSendMode(Fuzzer.SendMode.BULK);
            fuzzer.send(send);
            fuzzer.expect(expect);
            fuzzer.expectNoMoreFrames();
        }
        finally
        {
            fuzzer.close();
        }
    }
}
