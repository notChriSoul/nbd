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
        writer.writeInt32("_id", vm.getId());
        writer.writeInt32("rented", vm.getRented());
        writer.writeInt32("cpuCores", vm.getCPUCores());
        writer.writeDouble("ram", vm.getRAM());
        writer.writeDouble("storageSpace", vm.getStorageSpace());

        // Write subclass-specific fields
        switch (vm) {
            case Performance performance -> {
                writer.writeString("gpu", performance.getGPU());
                writer.writeBoolean("nvmestorage", true);
            }
            case Pro_oVirt proOVirt -> {
                writer.writeInt32("numaNodes", proOVirt.getNUMA_nodes());
                writer.writeBoolean("NumaArchitecture", true);
            }
            case Normal normal -> writer.writeBoolean("ssdSataStorage", true);
            default -> {
            }
        }

        writer.writeEndDocument();
    }

    @Override
    public VirtualMachine decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String type = null;
        int id = 0;
        int rented = 0;
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
                case "_id":
                    id = reader.readInt32();
                    break;
                case "rented":
                    rented = reader.readInt32();
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
        return switch (type) {
            case "normal" -> new Normal(id, cpuCores, ram, storageSpace);
            case "performance" -> new Performance(id, cpuCores, ram, storageSpace, gpu);
            case "proovirt" -> new Pro_oVirt(id, cpuCores, ram, storageSpace, numaNodes);
            case null, default -> throw new IllegalArgumentException("Unsupported virtual machine type: " + type);
        };
    }

    @Override
    public Class<VirtualMachine> getEncoderClass() {
        return VirtualMachine.class;
    }
}
