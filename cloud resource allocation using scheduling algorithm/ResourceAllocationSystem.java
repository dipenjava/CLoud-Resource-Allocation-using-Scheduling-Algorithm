import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

class Host {
    // Host properties
    private int id;
    private int bw;
    private int cpu;
    private int ram;
    private long storage;

    public Host(int id, int bw, int cpu, int ram, long storage) {
        this.id = id;
        this.bw = bw;
        this.cpu = cpu;
        this.ram = ram;
        this.storage = storage;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public int getBw() {
        return bw;
    }

    public int getCpu() {
        return cpu;
    }

    public int getRam() {
        return ram;
    }

    public long getStorage() {
        return storage;
    }
    
    // Helper method to check if a host meets the resource requirements
    public boolean meetsRequirements(int requiredBw, int requiredCpu, int requiredRam, long requiredStorage) {
        return (bw >= requiredBw && cpu >= requiredCpu && ram >= requiredRam && storage >= requiredStorage);
    }
}

class Datacenter {
    // Datacenter properties
    private String name;
    private List<Host> hostList;

    public Datacenter(String name) {
        this.name = name;
        this.hostList = new ArrayList<>();
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public List<Host> getHostList() {
        return hostList;
    }

    // Method to add hosts to the datacenter
    public void addHost(Host host) {
        hostList.add(host);
    }

    // Helper method to allocate resources to a task
    public boolean allocateResources(Host host, int requiredBw, int requiredCpu, int requiredRam, long requiredStorage) {
        // Allocation logic (you can implement specific logic here)
        if (host.meetsRequirements(requiredBw, requiredCpu, requiredRam, requiredStorage)) {
            System.out.println("Allocated to Host " + host.getId());
            return true; // Resources allocated successfully
        } else {
            return false; // Resources not available
        }
    }
}

class Task {
    // Task properties
    private String name;
    private boolean isHomogeneous;
    private int requiredServers;
    private int requiredBw;
    private int requiredCpu;
    private int requiredRam;
    private long requiredStorage;
    private Queue<Host> allocatedHosts;

    public Task(String name, boolean isHomogeneous, int requiredServers, int requiredBw, int requiredCpu, int requiredRam, long requiredStorage) {
        this.name = name;
        this.isHomogeneous = isHomogeneous;
        this.requiredServers = requiredServers;
        this.requiredBw = requiredBw;
        this.requiredCpu = requiredCpu;
        this.requiredRam = requiredRam;
        this.requiredStorage = requiredStorage;
        this.allocatedHosts = new LinkedBlockingQueue<>();
    }

    // Getter methods
    public String getName() {
        return name;
    }

    // Other getter methods (similar to above)
    
    // Helper method to allocate resources using SJF algorithm
    public boolean allocateResourcesSJF(Datacenter datacenter) {
        List<Host> availableHosts = new ArrayList<>(datacenter.getHostList());
        
        // Sort the availableHosts by the smallest job (CPU requirement)
        availableHosts.sort(Comparator.comparing(Host::getCpu));

        for (int i = 0; i < requiredServers; i++) {
            if (availableHosts.isEmpty()) {
                return false; // Resources not available
            }

            Host selectedHost = availableHosts.get(0); // Choose the host with the smallest CPU requirement

            if (datacenter.allocateResources(selectedHost, requiredBw, requiredCpu, requiredRam, requiredStorage)) {
                allocatedHosts.offer(selectedHost);
                availableHosts.remove(0);
            } else {
                return false; // Resources not available
            }
        }
        return true; // Resources allocated successfully
    }

    // Helper method to allocate resources using RR algorithm
    public boolean allocateResourcesRR(Datacenter datacenter, Queue<Host> availableHosts) {
        for (int i = 0; i < requiredServers; i++) {
            if (availableHosts.isEmpty()) {
                return false; // Resources not available
            }

            Host selectedHost = availableHosts.poll(); // Choose the host in a round-robin manner

            if (datacenter.allocateResources(selectedHost, requiredBw, requiredCpu, requiredRam, requiredStorage)) {
                allocatedHosts.offer(selectedHost);
            } else {
                return false; // Resources not available
            }
        }
        return true; // Resources allocated successfully
    }

    // Helper method to allocate resources using Max-Min algorithm
    public boolean allocateResourcesMaxMin(Datacenter datacenter) {
        // Max-Min allocation logic (implement as needed)
        // ...

        return true; // Resources allocated successfully
    }

    // Helper method to allocate resources using Min-Min algorithm
    public boolean allocateResourcesMinMin(Datacenter datacenter) {
        // Min-Min allocation logic (implement as needed)
        // ...

        return true; // Resources allocated successfully
    }

    // Helper method to allocate resources using Min-Max algorithm
    public boolean allocateResourcesMinMax(Datacenter datacenter) {
        // Min-Max allocation logic (implement as needed)
        // ...

        return true; // Resources allocated successfully
    }

    // Method to deallocate resources (for testing)
    public void deallocateResources(Datacenter datacenter) {
        for (Host host : allocatedHosts) {
            // Deallocate resources here (if needed)
            System.out.println("Deallocated from Host " + host.getId());
        }
        allocatedHosts.clear();
    }
}

public class ResourceAllocationSystem {
    public static void main(String[] args) {
        Datacenter datacenter = new Datacenter("Datacenter");

        // Add hosts to the datacenter
        datacenter.addHost(new Host(0, 10000, 4000, 8192, 1000000));
        datacenter.addHost(new Host(1, 20000, 6000, 16384, 2000000));

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter task details (or type 'exit' to quit):");

            System.out.print("Task name: ");
            String taskName = scanner.nextLine();

            if (taskName.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Is the task homogeneous (true/false)? ");
            boolean isHomogeneous = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("Number of servers required: ");
            int requiredServers = Integer.parseInt(scanner.nextLine());

            System.out.print("Required bandwidth (Mbps): ");
            int requiredBw = Integer.parseInt(scanner.nextLine());

            System.out.print("Required CPU (MIPS): ");
            int requiredCpu = Integer.parseInt(scanner.nextLine());

            System.out.print("Required RAM (GB): ");
            int requiredRam = Integer.parseInt(scanner.nextLine());

            System.out.print("Required storage (GB): ");
            long requiredStorage = Long.parseLong(scanner.nextLine());

            Task task = new Task(taskName, isHomogeneous, requiredServers, requiredBw, requiredCpu, requiredRam, requiredStorage);

            System.out.print("Select scheduling algorithm (SJF/RR/MaxMin/MinMin/MinMax): ");
            String schedulingAlgorithm = scanner.nextLine();

            boolean resourcesAllocated = false;
            switch (schedulingAlgorithm) {
                case "SJF":
                    resourcesAllocated = task.allocateResourcesSJF(datacenter);
                    break;
                case "RR":
                    Queue<Host> availableHostsRR = new LinkedBlockingQueue<>(datacenter.getHostList());
                    resourcesAllocated = task.allocateResourcesRR(datacenter, availableHostsRR);
                    break;
                case "MaxMin":
                    resourcesAllocated = task.allocateResourcesMaxMin(datacenter);
                    break;
                case "MinMin":
                    resourcesAllocated = task.allocateResourcesMinMin(datacenter);
                    break;
                case "MinMax":
                    resourcesAllocated = task.allocateResourcesMinMax(datacenter);
                    break;
                default:
                    System.out.println("Invalid scheduling algorithm.");
            }

            if (resourcesAllocated) {
                System.out.println("Resources allocated successfully.");
            } else {
                System.out.println(" Please wait.");
            }
        }
    }
}