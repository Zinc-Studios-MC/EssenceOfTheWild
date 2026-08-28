package net.mrmisc.essenceofthewild.entity.custom.rat;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class RatAnimations {
	public static final AnimationDefinition idle = AnimationDefinition.Builder.withLength(1.0F).looping()
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(-2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(2.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(34.6311F, 52.5796F, 42.0113F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(29.6996F, 54.8316F, 34.9036F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(30.6424F, 54.4687F, 36.0526F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(36.692F, 51.4057F, 43.6323F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(34.6311F, 52.5796F, 42.0113F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(34.6311F, -52.5796F, -42.0113F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(29.6996F, -54.8316F, -34.9036F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(30.6424F, -54.4687F, -36.0526F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(36.692F, -51.4057F, -43.6323F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(34.6311F, -52.5796F, -42.0113F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("back_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -5.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(-7.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, -0.3F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition walk = AnimationDefinition.Builder.withLength(1.0F).looping()
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(10.0F, 1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-7.5F, -1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(10.0F, -1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-7.5F, 1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_ear_cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.1001F, -9.5043F, 22.4896F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.3264F, 30.7462F, -9.0805F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(42.1861F, 55.2245F, 47.8137F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(22.1479F, 21.5992F, 34.9372F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.1001F, -9.5043F, 22.4896F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, -0.4F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.1861F, -55.2245F, -47.8137F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(22.1479F, -21.5992F, -34.9372F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.1001F, 9.5043F, -22.4896F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.3264F, -30.7462F, 9.0805F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(42.1861F, -55.2245F, -47.8137F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, -0.4F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("back_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(5.0F, -67.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, -45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(-10.0F, -22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(5.0F, 67.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 45.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(-10.0F, 22.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.1754F, 14.9416F, 1.3378F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(5.1754F, -14.9416F, -1.3378F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(5.1754F, 14.9416F, 1.3378F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_ear_cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_whiskey", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_whiskey", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-2.5F, -1.5625F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.375F, KeyframeAnimations.degreeVec(2.8125F, 1.6016F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.625F, KeyframeAnimations.degreeVec(-2.5F, 1.5625F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.875F, KeyframeAnimations.degreeVec(2.8125F, -1.6016F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition run = AnimationDefinition.Builder.withLength(0.3396F).looping()
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0424F, KeyframeAnimations.degreeVec(10.0F, 1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.degreeVec(-7.5F, -1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2122F, KeyframeAnimations.degreeVec(10.0F, -1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.degreeVec(-7.5F, 1.25F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_ear_cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0424F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2122F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.1001F, -9.5043F, 22.4896F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.3264F, 30.7462F, -9.0805F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(42.1861F, 55.2245F, 47.8137F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(22.1479F, 21.5992F, 34.9372F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.1001F, -9.5043F, 22.4896F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.1698F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.posVec(0.0F, -0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(42.1861F, -55.2245F, -47.8137F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(22.1479F, -21.5992F, -34.9372F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.1001F, 9.5043F, -22.4896F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.3264F, -30.7462F, 9.0805F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(42.1861F, -55.2245F, -47.8137F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.posVec(0.0F, -0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.posVec(0.0F, 0.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("back_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, -90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(-27.2356F, -30.0F, -54.7355F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.1698F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.2547F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(-27.2356F, 30.0F, 54.7355F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 90.0F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.0849F, KeyframeAnimations.posVec(0.0F, 1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.posVec(0.0F, 0.0F, -1.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.3396F, KeyframeAnimations.posVec(0.0F, 0.0F, 1.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(5.1754F, 14.9416F, 1.3378F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(5.1754F, -14.9416F, -1.3378F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(-5.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(5.1754F, 14.9416F, 1.3378F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_ear_cube", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0424F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2122F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -5.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("left_whiskey", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0424F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2122F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("right_whiskey", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0424F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2122F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -7.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 12.5F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0424F, KeyframeAnimations.degreeVec(-2.5F, -1.5625F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1273F, KeyframeAnimations.degreeVec(2.8125F, 1.6016F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.degreeVec(0.0F, 2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2122F, KeyframeAnimations.degreeVec(-2.5F, 1.5625F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2971F, KeyframeAnimations.degreeVec(2.8125F, -1.6016F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.degreeVec(0.0F, -2.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0849F, KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1698F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2547F, KeyframeAnimations.posVec(0.0F, 1.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3396F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();

	public static final AnimationDefinition lookout = AnimationDefinition.Builder.withLength(2.75F)
		.addAnimation("head", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(-15.38F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(44.243F, -0.1861F, -2.228F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(56.3071F, -0.1561F, -1.8691F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(85.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.75F, KeyframeAnimations.degreeVec(85.667F, 2.4977F, 29.9056F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.0F, KeyframeAnimations.degreeVec(85.667F, 2.4977F, 29.9056F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.25F, KeyframeAnimations.degreeVec(25.5003F, 1.8733F, 22.4292F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.5F, KeyframeAnimations.degreeVec(25.5003F, 1.8733F, 22.4292F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(1.75F, KeyframeAnimations.degreeVec(63.07F, -27.982F, -58.052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.25F, KeyframeAnimations.degreeVec(63.07F, -27.982F, -58.052F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(85.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.625F, KeyframeAnimations.degreeVec(-8.6294F, 1.7489F, 3.6283F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(33.5376F, 53.14F, 39.6395F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(40.8935F, 48.5904F, 49.1068F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(36.2041F, 51.7101F, 43.0033F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(48.0699F, 41.5607F, 59.2105F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(48.07F, -41.5607F, 120.7901F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, -37.5F, 112.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, -37.5F, 112.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.625F, KeyframeAnimations.degreeVec(1.5147F, 18.3951F, 15.1607F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(33.5376F, 53.14F, 39.6395F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_left_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(33.5376F, -53.14F, -39.6395F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.125F, KeyframeAnimations.degreeVec(40.8935F, -48.5904F, -49.1068F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(36.2041F, -51.7101F, -43.0033F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(48.0699F, -41.5607F, -59.2105F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.degreeVec(48.07F, 41.5607F, -120.7901F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(0.0F, 37.5F, -112.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(0.0F, 37.5F, -112.5F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.625F, KeyframeAnimations.degreeVec(1.5147F, -18.3951F, -15.1607F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(33.5376F, -53.14F, -39.6395F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("front_right_leg", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.2917F, KeyframeAnimations.posVec(0.0F, 0.9F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4583F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 2.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("back_left_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, -27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("back_right_leg", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 27.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("tail", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.4167F, KeyframeAnimations.degreeVec(79.92F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5F, KeyframeAnimations.degreeVec(87.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(97.5F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.0833F, KeyframeAnimations.degreeVec(1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.25F, KeyframeAnimations.degreeVec(-1.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.degreeVec(-8.75F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.degreeVec(-65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.degreeVec(-65.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.75F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.addAnimation("bone", new AnimationChannel(AnimationChannel.Targets.POSITION, 
			new Keyframe(0.0F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.1667F, KeyframeAnimations.posVec(0.0F, 0.6F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.3333F, KeyframeAnimations.posVec(0.0F, -1.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM),
			new Keyframe(0.5417F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.5F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.LINEAR),
			new Keyframe(2.75F, KeyframeAnimations.posVec(0.0F, 0.0F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
		))
		.build();
}