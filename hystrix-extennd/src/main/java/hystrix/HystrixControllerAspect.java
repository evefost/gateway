package hystrix;

import com.google.common.base.Throwables;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * @author xie
 */
@Aspect
public class HystrixControllerAspect {


	@Autowired(required = false)
	private HystrixStatusListener listener;

	@Around("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object methodsAnnotatedWithHystrixCommand(final ProceedingJoinPoint joinPoint) throws Throwable {

		Method method = getMethodFromTarget(joinPoint);
		RequestMappingInfo requestMappingInfo = RequestMappingInfo.methodMappings.get(method);
		if(requestMappingInfo == null){
			return joinPoint.proceed();
		}
		return new XHystrixCommand(requestMappingInfo, joinPoint.getTarget(), joinPoint.getArgs(),listener).execute();
	}

	public static Method getMethodFromTarget(JoinPoint joinPoint) {
		Method method = null;
		if (joinPoint.getSignature() instanceof MethodSignature) {
			MethodSignature signature = (MethodSignature) joinPoint.getSignature();
			method = getDeclaredMethod(joinPoint.getTarget().getClass(), signature.getName(),
				getParameterTypes(joinPoint));
		}
		return method;
	}

	/**
	 * Gets parameter types of the join point.
	 *
	 * @param joinPoint the join point
	 * @return the parameter types for the method this object
	 *         represents
	 */
	public static Class[] getParameterTypes(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		return method.getParameterTypes();
	}

	/**
	 * Gets declared method from specified type by mame and parameters types.
	 *
	 * @param type           the type
	 * @param methodName     the name of the method
	 * @param parameterTypes the parameter array
	 * @return a {@link Method} object or null if method doesn't exist
	 */
	public static Method getDeclaredMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
		Method method = null;
		try {
			method = type.getDeclaredMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException e) {
			Class<?> superclass = type.getSuperclass();
			if (superclass != null) {
				method = getDeclaredMethod(superclass, methodName, parameterTypes);
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return method;
	}

}