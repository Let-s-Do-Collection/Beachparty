package net.satisfy.beachparty.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

@Environment(EnvType.CLIENT)
public class FloatyBoatModel {
    public static void createChildren(PartDefinition partDefinition) {
        partDefinition.addOrReplaceChild("bottom", CubeListBuilder.create()
                        .texOffs(0, 0).mirror()
                        .addBox(-14.0F, -7.0F, -17.0F, 28.0F, 16.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 1.5708F, 0.0F, 0.0F));

        partDefinition.addOrReplaceChild("front", CubeListBuilder.create()
                        .texOffs(0, 29).mirror()
                        .addBox(-8.0F, -3.0F, -3.0F, 14.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

        partDefinition.addOrReplaceChild("back", CubeListBuilder.create()
                        .texOffs(0, 19).mirror()
                        .addBox(-6.0F, -3.0F, -3.0F, 14.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        partDefinition.addOrReplaceChild("right", CubeListBuilder.create()
                        .texOffs(0, 39).mirror()
                        .addBox(-14.0F, -3.0F, -3.0F, 28.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.0F, 0.0F, -3.1416F, 0.0F));

        partDefinition.addOrReplaceChild("left", CubeListBuilder.create()
                        .texOffs(0, 49).mirror()
                        .addBox(-14.0F, -3.0F, -1.0F, 28.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(0.0F, 0.0F, 9.0F));

        partDefinition.addOrReplaceChild("left_paddle", CubeListBuilder.create().texOffs(62, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(-1.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -5.0F, 9.0F, 0.0F, 0.0F, 0.19634955F));
        partDefinition.addOrReplaceChild("right_paddle", CubeListBuilder.create().texOffs(62, 0).addBox(-1.0F, 0.0F, -5.0F, 2.0F, 2.0F, 18.0F).addBox(0.001F, -3.0F, 8.0F, 1.0F, 6.0F, 7.0F), PartPose.offsetAndRotation(3.0F, -5.0F, -9.0F, 0.0F, 3.1415927F, 0.19634955F));

        partDefinition.addOrReplaceChild("water_patch", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-14.0F, -9.0F, -3.0F, 28.0F, 16.0F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -3.0F, 1.0F, 1.5707964F, 0.0F, 0.0F));
    }

    public static LayerDefinition createBodyModel() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        createChildren(partDefinition);
        return LayerDefinition.create(meshDefinition, 128, 64);
    }
}
