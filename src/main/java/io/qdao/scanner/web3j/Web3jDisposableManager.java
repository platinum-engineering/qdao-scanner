package io.qdao.scanner.web3j;

import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class Web3jDisposableManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Set<Disposable> disposables = Collections.synchronizedSet(new HashSet<>());

    public void add(Disposable disposable) {
        disposables.add(disposable);
    }

    public void addAll(Collection<Disposable> disposables) {
        this.disposables.addAll(disposables);
    }

    public void disposeAll() {
        for (Disposable disposable : disposables) {
            if (disposable.isDisposed()) {
                continue;
            }

            try {
                disposable.dispose();
            } catch (Throwable e) {
                logger.warn(String.format("[%s]\t:\t%s", disposable.getClass().getName(), e.getMessage()));
            }
        }
    }
}
