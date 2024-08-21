package dev.jonamireh.daggerkspcomponentbug.ksp.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import javax.annotation.processing.Generated

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class GenerateInjector

class InjectorGeneratorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor =
        InjectorGenerator(environment.codeGenerator)

    class InjectorGenerator(
        private val codeGenerator: CodeGenerator
    ) : SymbolProcessor {
        override fun process(resolver: Resolver): List<KSAnnotated> {
            resolver.getSymbolsWithAnnotation(requireNotNull(GenerateInjector::class.qualifiedName))
                .filterIsInstance<KSClassDeclaration>()
                .forEach {
                    it.injectorFileSpec.writeTo(
                        codeGenerator,
                        aggregating = false,
                        originatingKSFiles = listOf(requireNotNull(it.containingFile))
                    )
                }

            return emptyList()
        }

        private val KSClassDeclaration.injectorFileSpec: FileSpec
            get() {
                val fileSpecBuilder = FileSpec.builder(packageName.asString(), "${simpleName.asString()}Injector$")

                val typeSpec = TypeSpec.interfaceBuilder(requireNotNull(qualifiedName).getShortName() + "Injector")
                    .addAnnotation(
                        AnnotationSpec.builder(Generated::class).build(),
                    )
                    .addFunction(
                        FunSpec
                            .builder("inject")
                            .addModifiers(KModifier.ABSTRACT)
                            .addParameter(
                                ParameterSpec.builder(
                                    requireNotNull(qualifiedName).getShortName().decapitalize(),
                                    asType(emptyList()).toTypeName(),
                                ).build(),
                            ).build()
                    )
                    .build()

                    return fileSpecBuilder
                        .addAnnotation((AnnotationSpec.builder(Generated::class).build()))
                        .addType(typeSpec)
                        .build()
            }
    }
}