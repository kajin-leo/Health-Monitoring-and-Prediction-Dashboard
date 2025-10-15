import React, { useEffect, useState } from "react";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  Select,
  SelectItem,
} from "@heroui/react";
import FoodIntake from "./FoodIntake";
import FoodBarGraph from "./FoodBarGraph";
import type { GroupDatum } from "./FoodBarGraph";
import { apiClient } from "../../../service/axios";

const FoodIntakeWrapper: React.FC = () => {
  const [isOpen, setIsOpen] = useState(false);
  const [selectedGoal, setSelectedGoal] = useState("balanced");
  const [actualData, setActualData] = useState<GroupDatum[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const onOpen = () => setIsOpen(true);
  const onClose = () => setIsOpen(false);

  const fetchWeeklyIntake = async () => {
    setLoading(true);
    setError(null);

try {
  const res = await apiClient.get("/food-intake/intake-by-category");
  console.log("Calling intake-by-category with baseURL:", (apiClient as any).defaults?.baseURL);
  console.log("food-intake/intake-by-category response:", res);

  const formatted: GroupDatum[] = res.data.map((item: any) => ({
    group: item.group,          
    actual: item.actual,        
    recommended: 100,           // temporary placeholder, must be > 0 for bars to show
  }));


  setActualData(formatted);
} catch (err: any) {
      console.error(
        "food-intake/intake-by-category error:",
        err.response?.status,
        err.response?.data
      );

      setError(
        err.response
          ? `Error ${err.response.status}: ${err.response.data?.error || "Unknown"}`
          : err.message
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isOpen) {
      fetchWeeklyIntake();
    }
  }, [isOpen]);

  const dietGoals = [
    { key: "balanced", label: "Balanced Diet" },
    { key: "lean", label: "Lean Muscle Building" },
    { key: "cutting", label: "Weight Loss / Cutting" },
    { key: "bulking", label: "Bulking / Strength Gain" },
  ];

    return (
      <>
        <div onClick={onOpen}>
          <FoodIntake />
        </div>
  
        <Modal isOpen={isOpen} onClose={onClose} size="5xl">
          <ModalContent>
            <ModalHeader>Dietary Details</ModalHeader>
            <ModalBody className="h-fit space-y-4">
              {/* Diet selection menu */}
              <div className="flex justify-end mb-2 items-center gap-3">
                <Select
                  className="max-w-xs"
                  label="Select a Diet Goal"
                  selectedKeys={[selectedGoal]}
                  onSelectionChange={(keys) => {
                    const value = Array.from(keys)[0] as string;
                    setSelectedGoal(value);
                  }}
                >
                  {dietGoals.map((goal) => (
                    <SelectItem key={goal.key}>{goal.label}</SelectItem>
                  ))}
                </Select>
              </div>
  
              {/* Graph section */}
              {loading ? (
                <div className="text-center text-gray-500">Loading intake data...</div>
              ) : error ? (
                <div className="text-center text-red-500">
                  Failed to load data: {error}
                </div>
              ) : (
                <FoodBarGraph data={actualData} />
              )}
            </ModalBody>
          </ModalContent>
        </Modal>
  
      </>
    );
  };
  
  export default FoodIntakeWrapper;
