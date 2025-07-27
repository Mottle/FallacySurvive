package dev.deepslate.fallacy.survive.client.screen

import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.components.interactable.Button
import com.github.wintersteve25.tau.components.interactable.ListView
import com.github.wintersteve25.tau.components.layout.Align
import com.github.wintersteve25.tau.components.layout.Center
import com.github.wintersteve25.tau.components.layout.Column
import com.github.wintersteve25.tau.components.layout.Row
import com.github.wintersteve25.tau.components.utils.Container
import com.github.wintersteve25.tau.components.utils.Sized
import com.github.wintersteve25.tau.components.utils.Text
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.layout.LayoutSetting
import com.github.wintersteve25.tau.theme.Theme
import com.github.wintersteve25.tau.utils.Color
import com.github.wintersteve25.tau.utils.FlexSizeBehaviour
import com.github.wintersteve25.tau.utils.Size
import dev.deepslate.fallacy.base.client.screen.UIContext
import dev.deepslate.fallacy.base.client.screen.component.ContextWrapperUI
import dev.deepslate.fallacy.base.client.screen.component.ProcessBar
import dev.deepslate.fallacy.survive.ModAttachments
import dev.deepslate.fallacy.survive.diet.NutrientType
import dev.deepslate.fallacy.survive.diet.alternativeAttribute
import dev.deepslate.fallacy.survive.diet.attribute
import dev.deepslate.fallacy.survive.diet.entity.LivingEntityNutritionState
import dev.deepslate.fallacy.utils.RGB
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import kotlin.math.max

class DietUI : ContextWrapperUI() {
    override fun build(context: UIContext, layout: Layout, theme: Theme): UIComponent {

        val list = Align.Builder().withHorizontal(LayoutSetting.CENTER).build(getList())
        val title = getTitle()
//        val t = ColoredTexture.Builder(Fallacy.id("textures/gui/title.png")).withColor(RGB.fromHex("0xf4fc03"))
//            .withTextureSize(SimpleVec2i(100, 15)).withUvSize(SimpleVec2i(100, 15)).withSize(SimpleVec2i(100, 15))

        val button = Align.Builder().withHorizontal(LayoutSetting.CENTER)
            .build(
                Sized(
                    Size.staticSize(80, 25),
                    Button.Builder().withOnPress { context.displayPreviousUI() }.build(Center(Text.Builder("OK")))
                )
            )
        val col = Column.Builder().withSpacing(5).withSizeBehaviour(FlexSizeBehaviour.MAX).build(title, list, button)

        val window = Container.Builder().withChild(col)
        return Center(Sized(Size.staticSize(300, 200), window))
    }

    private var nutrientState: LivingEntityNutritionState? = null

    fun withExistedNutritionState(nutrition: LivingEntityNutritionState): DietUI {
        this.nutrientState = nutrition
        return this
    }

    private fun getTitle() = Sized(
        Size.percentage(1f, 0.2f),
        Center(Text.Builder(Component.translatable("gui.fallacy_survive.diet_ui.title")).withColor(Color.BLACK))
    )

    private fun getList(): UIComponent {
        val nutrition = nutrientState ?: Minecraft.getInstance().player?.getData(ModAttachments.NUTRITION_STATE)
        ?: LivingEntityNutritionState()
//        val carbohydrate = if (nutrition.carbohydrate is LivingEntityNutritionState.Nutrition.State) DescriptionProcessBar(
//            "item.fallacy.diet_data.carbohydrate",
//            nutrition.carbohydrate.value.toInt(),
//            RGB.fromHex("0xff4500")
//        ) else null
//        val protein = if (nutrition.protein is LivingEntityNutritionState.Nutrition.State) DescriptionProcessBar(
//            "item.fallacy.diet_data.protein",
//            nutrition.protein.value.toInt(),
//            RGB.fromHex("0xffa500")
//        ) else null
//        val fat = if (nutrition.fat is LivingEntityNutritionState.Nutrition.State) DescriptionProcessBar(
//            "item.fallacy.diet_data.fat",
//            nutrition.fat.value.toInt(),
//            RGB.fromHex("0x8b7e66")
//        ) else null
//        val fiber =
//            if (nutrition.fiber is LivingEntityNutritionState.Nutrition.State) DescriptionProcessBar(
//                "item.fallacy.diet_data.fiber",
//                nutrition.fiber.value.toInt(),
//                RGB.fromHex("0x32cd32")
//            ) else null
//        val electrolyte = if (nutrition.electrolyte is LivingEntityNutritionState.Nutrition.State) DescriptionProcessBar(
//            "item.fallacy.diet_data.electrolyte",
//            nutrition.electrolyte.value.toInt(),
//            RGB.fromHex("0x00ced1")
//        ) else null
//        val array = arrayOf(carbohydrate, protein, fat, fiber, electrolyte).filterNotNull()
        val array = nutrition.map { (type, value) ->
            //type序列化时不带color
            val sourceType = NutrientType.REGISTRY.get(type.id) ?: type
            val player = Minecraft.getInstance().player!!

            val attributeValue = with(sourceType) { attribute ?: alternativeAttribute!! }.let(player::getAttributeValue)

            DescriptionProcessBar(
                sourceType.component,
                value.toInt(),
                attributeValue.toInt(),
                RGB.from(sourceType.displayColor)
            )
        }

        val height = 100 + max(array.size * 20 - 50, 0)

        return Sized(Size.staticSize(220, height), ListView.Builder().withSpacing(10).build(array))
    }

//    class DescriptionProcessBar(val description: String, val ratio: Int, val color: RGB = RGB.fromHex("0x32cd32")) :
//        UIComponent {
//        override fun build(
//            layout: Layout,
//            theme: Theme
//        ): UIComponent = Sized(
//            Size.staticSize(220, 10), Row.Builder().withSpacing(5).withSizeBehaviour(FlexSizeBehaviour.MAX)
//                .build(
//                    Align.Builder().withHorizontal(LayoutSetting.START).withVertical(LayoutSetting.CENTER).build(
//                        Sized(
//                            Size.staticSize(100, 10),
//                            Text.Builder(Component.translatable(description).append(Component.literal(": $ratio/100")))
//                                .withColor(Color(color.value))
//                        )
//                    ), ProcessBar(ratio, color)
//                )
//        )
//    }

    class DescriptionProcessBar(
        val description: MutableComponent,
        val value: Int,
        val max: Int,
        val color: RGB
    ) :
        UIComponent {
        override fun build(
            layout: Layout,
            theme: Theme
        ): UIComponent = Sized(
            Size.staticSize(220, 10), Row.Builder().withSpacing(5).withSizeBehaviour(FlexSizeBehaviour.MAX)
                .build(
                    Align.Builder().withHorizontal(LayoutSetting.START).withVertical(LayoutSetting.CENTER).build(
                        Sized(
                            Size.staticSize(100, 10),
                            Text.Builder(description.copy().append(Component.literal(": $value/$max")))
                                .withColor(Color(color.value))
                        )
                    ), ProcessBar(value, color)
                )
        )
    }
}