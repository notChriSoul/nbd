package org.example.vms;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;


public class VirtualMachineCodec implements Codec<VirtualMachine> {

    @Override
    public void encode(BsonWriter writer, VirtualMachine vm, EncoderContext encoderContext) {
        writer.writeStartDocument();

        // Write the type discriminator based on the class
        if (vm instanceof Normal) {
            writer.writeString("_type", "normal");
        } else if (vm instanceof Performance) {
            writer.writeString("_type", "performance");
        } else if (vm instanceof Pro_oVirt) {
            writer.writeString("_type", "proovirt");
        }

        // Write common fields for all VirtualMachine types
        writer.writeInt32("id", vm.getId());
        writer.writeBoolean("isAvailable", vm.isAvailable());
        writer.writeInt32("cpuCores", vm.getCPUCores());
        writer.writeDouble("ram", vm.getRAM());
        writer.writeDouble("storageSpace", vm.getStorageSpace());

        // Write subclass-specific fields
        if (vm instanceof Performance) {
            writer.writeString("gpu", ((Performance) vm).getGPU());
            writer.writeBoolean("nvmestorage", true);
        } else if (vm instanceof Pro_oVirt) {
            writer.writeInt32("numaNodes", ((Pro_oVirt) vm).getNUMA_nodes());
            writer.writeBoolean("NumaArchitecture", true);
        } else if (vm instanceof Normal) {
            writer.writeBoolean("ssdSataStorage", true);
        }

        writer.writeEndDocument();
    }

    @Override
    public VirtualMachine decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String type = null;
        int id = 0;
        boolean isAvailable = false;
        int cpuCores = 0;
        double ram = 0.0;
        double storageSpace = 0.0;
        String gpu = null;
        int numaNodes = 0;

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            switch (fieldName) {
                case "_type":
                    type = reader.readString();
                    break;
                case "id":
                    id = reader.readInt32();
                    break;
                case "isAvailable":
                    isAvailable = reader.readBoolean();
                    break;
                case "cpuCores":
                    cpuCores = reader.readInt32();
                    break;
                case "ram":
                    ram = reader.readDouble();
                    break;
                case "storageSpace":
                    storageSpace = reader.readDouble();
                    break;
                case "gpu":
                    gpu = reader.readString();
                    break;
                case "numaNodes":
                    numaNodes = reader.readInt32();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.readEndDocument();

        // Instantiate the correct subclass based on the "_type" field
        if ("normal".equals(type)) {
            return new Normal(id, isAvailable, cpuCores, ram, storageSpace);
        } else if ("performance".equals(type)) {
            return new Performance(id, isAvailable, cpuCores, ram, storageSpace, gpu);
        } else if ("proovirt".equals(type)) {
            return new Pro_oVirt(id, isAvailable, cpuCores, ram, storageSpace, numaNodes);
        } else {
            throw new IllegalArgumentException("Unsupported virtual machine type: " + type);
        }
    }

    @Override
    public Class<VirtualMachine> getEncoderClass() {
        return VirtualMachine.class;
    }
}
