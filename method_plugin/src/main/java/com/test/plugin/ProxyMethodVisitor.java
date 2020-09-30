package com.test.plugin;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class ProxyMethodVisitor extends AdviceAdapter {
    private static final String ANNOTATION_TRACK_METHOD = "Lcom/test/lib/TrackMethod;";
    private static final String METHOD_EVENT_MANAGER = "com/test/lib/MethodEventManager";
    /**
     * 后期通过它来对class文件进行织入代码
     */
    private MethodVisitor methodVisitor;
    /**
     * 后期将其传递给MethodEventManager从而进行通知
     */
    private String methodName;

    private boolean needInject;
    private String tag;

    public ProxyMethodVisitor(MethodVisitor methodVisitor, int access, String name, String desc) {
        super(Opcodes.ASM6, methodVisitor, access, name, desc);
        this.methodVisitor = methodVisitor;
        this.methodName = name;
    }

    /**
     * 重写visitAnnotation方法来在访问方法的注解时进行处理，
     * 从而判断该方法是否需要织入，同时获取注解中的tag
     */
    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(descriptor, visible);
        if (descriptor.equals(ANNOTATION_TRACK_METHOD)) {
            needInject = true;
            return new AnnotationVisitor(Opcodes.ASM6, annotationVisitor) {
                @Override
                public void visit(String name, Object value) {
                    super.visit(name, value);
                    if (name.equals("tag") && value instanceof String) {
                        tag = (String) value;
                    }
                }
            };
        }
        return annotationVisitor;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        handleMethodEnter();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        handleMethodExit();
    }

    /**
     * 植入我们想要的代码
     */
    private void handleMethodEnter() {
        if (needInject && tag != null) {
            // 通过MethodWriter.visitMethodInsn方法可以调用类似字节码的指令来调用方法
            // 比如visitMethodInsn(INVOKESTATIC, 类签名, 方法名, 方法签名);
            methodVisitor.visitMethodInsn(
                    INVOKESTATIC,
                    METHOD_EVENT_MANAGER,
                    "getInstance",
                    "()L" + METHOD_EVENT_MANAGER + ";",
                    false
            );
            methodVisitor.visitLdcInsn(tag);
            methodVisitor.visitLdcInsn(methodName);
            methodVisitor.visitMethodInsn(
                    INVOKEVIRTUAL,
                    METHOD_EVENT_MANAGER,
                    "notifyMethodEnter",
                    "(Ljava/lang/String;Ljava/lang/String;)V",
                    false
            );
        }
    }


    private void handleMethodExit() {
        if (needInject && tag != null) {
            methodVisitor.visitMethodInsn(
                    INVOKESTATIC,
                    METHOD_EVENT_MANAGER,
                    "getInstance",
                    "()L" + METHOD_EVENT_MANAGER + ";",
                    false
            );
            methodVisitor.visitLdcInsn(tag);
            methodVisitor.visitLdcInsn(methodName);
            methodVisitor.visitMethodInsn(
                    INVOKEVIRTUAL,
                    METHOD_EVENT_MANAGER,
                    "notifyMethodExit",
                    "(Ljava/lang/String;Ljava/lang/String;)V",
                    false
            );
        }
    }
}
