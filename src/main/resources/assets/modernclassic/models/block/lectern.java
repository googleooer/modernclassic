public VoxelShape makeShape(){
	VoxelShape shape = VoxelShapes.empty();
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 0, 0, 1, 0.0625, 1));
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.125, 0.0625, 0.125, 0.875, 0.125, 0.875));
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0.1875, 0.125, 0.1875, 0.8125, 1.125, 0.8125));
	shape = VoxelShapes.union(shape, VoxelShapes.cuboid(0, 1.125, 0, 1, 1.1875, 1));

	return shape;
}