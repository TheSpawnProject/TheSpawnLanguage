package net.programmer.igoodie.tsl.parser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.util.ArrayDeque;
import java.util.Deque;

public class CharStream {

    private final Reader reader;
    private final Deque<Character> lookaheadBuffer;
    private boolean endOfStream;

    public CharStream(Reader reader) {
        this.reader = reader;
        this.lookaheadBuffer = new ArrayDeque<>();
        this.endOfStream = false;
    }

    public static CharStream fromString(String input) {
        return new CharStream(new BufferedReader(new java.io.StringReader(input)));
    }

    public static CharStream fromFile(File file) throws IOException {
        return new CharStream(new BufferedReader(new FileReader(file)));
    }

    public static CharStream fromByteChannel(ByteChannel byteChannel) {
        return new CharStream(new BufferedReader(new ByteChannelReader(byteChannel)));
    }

    public char peek(int k) throws IOException {
        fillBufferUpTo(k);

        if (k <= lookaheadBuffer.size()) {
            return lookaheadBuffer.stream().skip(k - 1).findFirst().orElse('\0');
        }

        return '\0';
    }

    public char peek() throws IOException {
        return peek(1);
    }

    public void consume(int k) throws IOException {
        fillBufferUpTo(k);

        for (int i = 0; i < k && !lookaheadBuffer.isEmpty(); i++) {
            lookaheadBuffer.poll();
        }
    }

    public void consume() throws IOException {
        consume(1);
    }

    private void fillBufferUpTo(int k) throws IOException {
        while (lookaheadBuffer.size() < k && !endOfStream) {
            int nextChar = reader.read();
            if (nextChar == -1) {
                endOfStream = true;
                break;
            }
            lookaheadBuffer.add((char) nextChar);
        }
    }

    public boolean hasNext() throws IOException {
        fillBufferUpTo(1);
        return !lookaheadBuffer.isEmpty() || !endOfStream;
    }

    private static class ByteChannelReader extends Reader {
        private final ByteChannel channel;
        private final ByteBuffer buffer;

        public ByteChannelReader(ByteChannel channel) {
            this.channel = channel;
            this.buffer = ByteBuffer.allocate(1024); // Buffer size for reading
            this.buffer.flip(); // Initially flip for reading
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            int bytesRead = 0;
            while (len > 0) {
                if (!buffer.hasRemaining()) {
                    buffer.clear();
                    int n = channel.read(buffer);
                    if (n == -1) {
                        return bytesRead == 0 ? -1 : bytesRead; // EOF or return number of bytes read
                    }
                    buffer.flip();
                }

                while (buffer.hasRemaining() && len > 0) {
                    cbuf[off++] = (char) buffer.get(); // Cast byte to char (UTF-8 assumed)
                    bytesRead++;
                    len--;
                }
            }
            return bytesRead;
        }

        @Override
        public void close() throws IOException {
            channel.close();
        }
    }

}

