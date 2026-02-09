package com.zr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/async")
public class AsyncController {

	@GetMapping("/callable")
	public Callable<String> asyncWithCallable() {
		return () -> {
			Thread.sleep(3000);  // 模拟耗时操作
			return "Async result after 3 seconds";
		};
	}

	@GetMapping("/deferred")
	public DeferredResult<String> asyncWithDeferred() {
		DeferredResult<String> deferredResult = new DeferredResult<>();

		CompletableFuture.runAsync(() -> {
			try {
				Thread.sleep(3000);
				deferredResult.setResult("Deferred result after 3 seconds");
			} catch (InterruptedException e) {
				deferredResult.setErrorResult(e);
			}
		});

		return deferredResult;
	}

}
