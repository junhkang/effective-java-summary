package org.example.ch01.item08.codes;

public class Resource implements AutoCloseable {
    private boolean closed = false;

    public void doSomething() {
        if (closed) {
            throw new IllegalStateException("Resource is closed");
        }
    }

    @Override
    public void close() {
        closed = true;
        // 그 외 자원 정리 작업 수행, 예: 파일 닫기, 네트워크 연결 해제
    }
}