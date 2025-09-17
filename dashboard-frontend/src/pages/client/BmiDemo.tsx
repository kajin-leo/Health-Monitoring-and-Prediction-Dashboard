import BmiCard from "../../components/DashboardCardContent/Bmi/BmiCard";

export default function BmiDemo() {
  return (
    <div style={{ minHeight: "100vh", padding: 16, background: "#f1f5f9" }}>
      <h1 style={{ fontSize: 24, fontWeight: 700, marginBottom: 16 }}>
        BMI Card Demo
      </h1>
      <BmiCard bmi={22} waist={150} height={175} weight={70} width={500} showLabels={true} />
    </div>
  );
}
